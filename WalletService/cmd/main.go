package main

import (
	"context"
	"encoding/json"
	"log"
	"net/http"
	"os"
	"os/signal"
	"sync"
	"syscall"
	
	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
	"github.com/gin-gonic/gin"

	"dompet-digital-microservice/internal/wallet"
	"dompet-digital-microservice/pkg/config"
	"dompet-digital-microservice/pkg/database"
	kafkaPkg "dompet-digital-microservice/pkg/kafka"
)

func main() {
	config.LoadConfig()

	gormDB := database.NewGormDB()
	sqlDB, err := gormDB.DB()
	if err != nil {
		log.Fatal("Failed to get underlying sql.DB")
	}
	defer sqlDB.Close()

	producer := kafkaPkg.NewProducer()
	defer producer.Close()

	walletRepo := wallet.NewRepository(gormDB)
	walletService := wallet.NewService(walletRepo, producer, os.Getenv("KAFKA_TOPIC_WALLET_UPDATED"))
	walletHandler := wallet.NewHandler(walletService)

	ctx, cancel := context.WithCancel(context.Background())
	var wg sync.WaitGroup
	consumerGroupID := os.Getenv("KAFKA_CONSUMER_GROUP")

	topicUserCreated := os.Getenv("KAFKA_TOPIC_USER_CREATED")
	kafkaPkg.StartConsumer(ctx, &wg, topicUserCreated, consumerGroupID, func(ctx context.Context, msg *kafka.Message) error {
		var event wallet.UserCreatedEvent
		if err := json.Unmarshal(msg.Value, &event); err != nil {
			log.Printf("ERROR: Failed to unmarshal UserCreatedEvent: %v", err)
			return err
		}
		return walletService.HandleUserCreated(ctx, event)
	})

	topicTopupSuccess := os.Getenv("KAFKA_TOPIC_TOPUP_SUCCESS")
	kafkaPkg.StartConsumer(ctx, &wg, topicTopupSuccess, consumerGroupID, func(ctx context.Context, msg *kafka.Message) error {
		var event wallet.TopupSuccessEvent
		if err := json.Unmarshal(msg.Value, &event); err != nil {
			log.Printf("ERROR: Failed to unmarshal TopupSuccessEvent: %v", err)
			return err
		}
		return walletService.HandleTopupSuccess(ctx, event)
	})

	router := gin.Default()
	walletHandler.RegisterRoutes(router)

	appPort := os.Getenv("APP_PORT")
	if appPort == "" {
		appPort = "8083"
	}

	go func() {
		log.Printf("Wallet Service API is running on port %s", appPort)
		if err := router.Run(":" + appPort); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Failed to run server: %v", err)
		}
	}()

	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	log.Println("Shutting down server and consumers...")

	cancel()
	wg.Wait()

	log.Println("Server gracefully stopped.")
}