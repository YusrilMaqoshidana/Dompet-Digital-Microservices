package kafka

import (
	"context"
	"dompet-digital-microservice/internal/wallet"
	"encoding/json"
	"fmt"
	"os"
	"strings"

	"github.com/segmentio/kafka-go"
)

func NewConsumer(topic, groupID string) *kafka.Reader {
	brokers := strings.Split(os.Getenv("KAFKA_BROKERS"), ",")
	return kafka.NewReader(kafka.ReaderConfig{
		Brokers:  brokers,
		GroupID:  groupID,
		Topic:    topic,
		MinBytes: 10e3,
		MaxBytes: 10e6,
	})
}

func ConsumeUserCreatedEvents(ctx context.Context, service wallet.Service) {
	topic := os.Getenv("KAFKA_TOPIC_USER_CREATED")
	groupID := os.Getenv("KAFKA_CONSUMER_GROUP")
	reader := NewConsumer(topic, groupID)
	defer reader.Close()

	fmt.Printf("Starting consumer for topic: %s", topic)
	for {
		msg, err := reader.ReadMessage(ctx)
		if err != nil {
			fmt.Printf("ERROR - could not read message from %s: %v", topic, err)
			break
		}

		var event wallet.UserCreatedEvent
		if err := json.Unmarshal(msg.Value, &event); err != nil {
			fmt.Printf("ERROR - failed to unmarshal UserCreatedEvent: %v", err)
			continue
		}

		if err := service.HandleUserCreated(ctx, event); err != nil {
			fmt.Printf("ERROR - failed to handle UserCreatedEvent: %v", err)
		}
	}
}

func ConsumeTopupSuccessEvents(ctx context.Context, service wallet.Service) {
    topic := os.Getenv("KAFKA_TOPIC_TOPUP_SUCCESS")
    groupID := os.Getenv("KAFKA_CONSUMER_GROUP")
    reader := NewConsumer(topic, groupID)
    defer reader.Close()

    fmt.Printf("Starting consumer for topic: %s", topic)
    for {
        msg, err := reader.ReadMessage(ctx)
        if err != nil {
            fmt.Printf("ERROR - could not read message from %s: %v", topic, err)
            break
        }

        var event wallet.TopupSuccessEvent
        if err := json.Unmarshal(msg.Value, &event); err != nil {
            fmt.Printf("ERROR - failed to unmarshal TopupSuccessEvent: %v", err)
            continue
        }

        if err := service.HandleTopupSuccess(ctx, event); err != nil {
            fmt.Printf("ERROR - failed to handle TopupSuccessEvent: %v", err)
        }
    }
}