package kafka

import (
	"context"
	"log"
	"os"
	"strings"
	"sync" // Kita butuh WaitGroup untuk shutdown yang bersih

	"github.com/segmentio/kafka-go"
)

// Fungsi NewConsumer Anda sudah bagus, kita pertahankan.
func NewConsumer(topic, groupID string) *kafka.Reader {
	brokers := strings.Split(os.Getenv("KAFKA_BROKERS"), ",")
	if len(brokers) == 1 && brokers[0] == "" {
		log.Fatal("KAFKA_BROKERS environment variable is not set or empty")
	}
	
	return kafka.NewReader(kafka.ReaderConfig{
		Brokers:  brokers,
		GroupID:  groupID,
		Topic:    topic,
		MinBytes: 10e3, // 10KB
		MaxBytes: 10e6, // 10MB
	})
}

// Ini adalah fungsi consumer generik kita.
// Ia menerima topic, groupID, dan fungsi handler sebagai parameter.
func StartConsumer(ctx context.Context, wg *sync.WaitGroup, topic string, groupID string, handlerFunc func(ctx context.Context, msg kafka.Message) error) {
	wg.Add(1) // Menandakan kita memulai satu goroutine baru

	go func() {
		defer wg.Done() // Menandakan goroutine ini selesai saat fungsi berakhir
		
		reader := NewConsumer(topic, groupID)
		defer reader.Close()

		log.Printf("INFO - Starting Kafka consumer for topic: [%s], group: [%s]", topic, groupID)
		
		for {
			// Menggunakan select untuk bisa berhenti dengan anggun (graceful shutdown)
			select {
			case <-ctx.Done():
				log.Printf("INFO - Stopping consumer for topic: [%s]", topic)
				return
			default:
				msg, err := reader.ReadMessage(ctx)
				if err != nil {
					// Jika context di-cancel, error ini akan muncul, jadi kita keluar saja.
					if ctx.Err() != nil {
						return
					}
					log.Printf("ERROR - Could not read message from topic %s: %v", topic, err)
					continue // Jangan hentikan loop, coba lagi
				}

				// Panggil fungsi handler yang spesifik untuk topik ini
				err = handlerFunc(ctx, msg)
				if err != nil {
					log.Printf("ERROR - Failed to handle message from topic %s: %v", topic, err)
					// Di sini Anda bisa menambahkan logika untuk mengirim ke Dead Letter Queue (DLQ)
				}
			}
		}
	}()
}