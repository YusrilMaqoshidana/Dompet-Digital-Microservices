// pkg/kafka/producer.go
package kafka

import (
	"context"
	"os"
	"strings"
	"time"

	"github.com/segmentio/kafka-go"
)

type Producer struct {
	writer *kafka.Writer
}

func NewProducer() *Producer {
	brokers := strings.Split(os.Getenv("KAFKA_BROKERS"), ",")

	writer := &kafka.Writer{
		Addr:         kafka.TCP(brokers...),
		Balancer:     &kafka.LeastBytes{},
		RequiredAcks: kafka.RequireAll, // Menjamin pesan sampai ke semua replica
		WriteTimeout: 10 * time.Second,
		ReadTimeout:  10 * time.Second,
	}
	return &Producer{writer: writer}
}

func (p *Producer) Publish(ctx context.Context, topic string, key []byte, value []byte) error {
	return p.writer.WriteMessages(ctx, kafka.Message{
		Topic: topic,
		Key:   key,
		Value: value,
	})
}

func (p *Producer) Close() error {
	return p.writer.Close()
}