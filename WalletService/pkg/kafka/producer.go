package kafka

import (
	"context"
	"log"
	"os"

	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
)

type Producer struct {
	p *kafka.Producer
}

func NewProducer() *Producer {
	kafkaBrokers := os.Getenv("KAFKA_BROKERS")
	if kafkaBrokers == "" {
		log.Fatal("FATAL: KAFKA_BROKERS environment variable is not set!")
	}

	p, err := kafka.NewProducer(&kafka.ConfigMap{"bootstrap.servers": kafkaBrokers})
	if err != nil {
		log.Fatalf("Failed to create Kafka producer: %s", err)
	}

	log.Println("INFO: Kafka producer created successfully")
	return &Producer{p: p}
}

func (p *Producer) Publish(ctx context.Context, topic string, key []byte, value []byte) error {
	deliveryChan := make(chan kafka.Event)
	defer close(deliveryChan)

	err := p.p.Produce(&kafka.Message{
		TopicPartition: kafka.TopicPartition{Topic: &topic, Partition: int32(kafka.PartitionAny)},
		Key:            key,
		Value:          value,
	}, deliveryChan)

	if err != nil {
		log.Printf("ERROR: Produce failed: %v\n", err)
		return err
	}

	e := <-deliveryChan
	m := e.(*kafka.Message)

	if m.TopicPartition.Error != nil {
		log.Printf("ERROR: Delivery failed: %v\n", m.TopicPartition.Error)
		return m.TopicPartition.Error
	}

	log.Printf("INFO: Delivered message to topic %s [%d] at offset %v\n",
		*m.TopicPartition.Topic, m.TopicPartition.Partition, m.TopicPartition.Offset)

	return nil
}

func (p *Producer) Close() {
	p.p.Flush(15 * 1000)
	p.p.Close()
}