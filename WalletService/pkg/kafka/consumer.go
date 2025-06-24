package kafka

import (
	"context"
	"log"
	"os"
	"sync"
	"time"

	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
)

func StartConsumer(ctx context.Context, wg *sync.WaitGroup, topic string, groupID string, handlerFunc func(ctx context.Context, msg *kafka.Message) error) {
	wg.Add(1)
	go func() {
		defer wg.Done()

		kafkaBrokers := os.Getenv("KAFKA_BROKERS")
		if kafkaBrokers == "" {
			log.Fatalf("FATAL: KAFKA_BROKERS environment variable is not set!")
		}

		c, err := kafka.NewConsumer(&kafka.ConfigMap{
			"bootstrap.servers":       kafkaBrokers,
			"group.id":                groupID,
			"auto.offset.reset":       "earliest",
			"enable.auto.commit":      true,
		})

		if err != nil {
			log.Fatalf("FATAL: Failed to create Kafka consumer: %v", err)
		}
		defer c.Close()

		err = c.SubscribeTopics([]string{topic}, nil)
		if err != nil {
			log.Printf("FATAL: Failed to subscribe to topic %s: %v", topic, err)
			return
		}

		log.Printf("INFO: Kafka consumer started for topic [%s], group [%s]", topic, groupID)

		run := true
		for run {
			select {
			case <-ctx.Done():
				log.Printf("INFO: Stopping consumer for topic: [%s]", topic)
				run = false
			default:
				msg, err := c.ReadMessage(100 * time.Millisecond)

				if err != nil {
					if kafkaErr, ok := err.(kafka.Error); ok && kafkaErr.Code() == kafka.ErrTimedOut {
						continue
					}
					log.Printf("ERROR: Consumer error on topic %s: %v\n", topic, err)
					continue
				}

				log.Printf("INFO: Received message on topic %s: Partition %d, Offset %d", *msg.TopicPartition.Topic, msg.TopicPartition.Partition, msg.TopicPartition.Offset)
				if handlerErr := handlerFunc(ctx, msg); handlerErr != nil {
					log.Printf("ERROR: Failed to handle message from topic %s: %v", topic, handlerErr)
				}
			}
		}
	}()
}