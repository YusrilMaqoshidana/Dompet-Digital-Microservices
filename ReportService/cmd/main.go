package main

import (
	"context"
	"encoding/json"
	"log"
	"net/http"
	"os"
	"os/signal"
	"report-service/internal/report"
	"report-service/pkg/config"
	"report-service/pkg/database"
	kafkapkg "report-service/pkg/kafka"
	"sync"
	"syscall"

	"github.com/gin-gonic/gin"
	"github.com/segmentio/kafka-go"
)

func main() {
	config.LoadConfig()

	db := database.NewGormDB()
	reportRepo := report.NewRepository(db)
	reportService := report.NewService(reportRepo)
	reportHandler := report.NewHandler(reportService)

	ctx, cancel := context.WithCancel(context.Background())
	var wg sync.WaitGroup
	
	groupID := "report-service-group"
	
	topicSuccess := os.Getenv("KAFKA_TOPIC_TOPUP_SUCCESS")
	kafkapkg.StartConsumer(ctx, &wg, topicSuccess, groupID, func(ctx context.Context, msg kafka.Message) error {
		var event report.TopupEvent
		if err := json.Unmarshal(msg.Value, &event); err != nil {
			log.Printf("ERROR - Failed to unmarshal TopupEvent on topic %s: %v", msg.Topic, err)
			return err
		}
		return reportService.ProcessTopupSuccessEvent(ctx, event)
	})

	topicFailed := os.Getenv("KAFKA_TOPIC_TOPUP_FAILED")
	kafkapkg.StartConsumer(ctx, &wg, topicFailed, groupID, func(ctx context.Context, msg kafka.Message) error {
		var event report.TopupEvent
		if err := json.Unmarshal(msg.Value, &event); err != nil {
			log.Printf("ERROR - Failed to unmarshal TopupEvent on topic %s: %v", msg.Topic, err)
			return err
		}
		return reportService.ProcessTopupFailedEvent(ctx, event)
	})

	// topicTransactionSuccess := os.Getenv("KAFKA_TOPIC_TRANSACTION_SUCCESS")
	// kafkapkg.StartConsumer(ctx, &wg, topicTransactionSuccess, groupID, func(ctx context.Context, msg kafka.Message) error {
	// 	 var event report.TransactionEvent
	// 	 // ... unmarshal dan panggil service.ProcessTransactionSuccessEvent ...
	// 	 return nil
	// })

	router := gin.Default()
	reportHandler.RegisterRoutes(router)

	appPort := os.Getenv("APP_PORT")
	if appPort == "" {
		appPort = "8085"
	}

	go func() {
		log.Printf("Report Service API is running on port %s", appPort)
		if err := router.Run(":" + appPort); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Failed to run server: %v", err)
		}
	}()

	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	log.Println("Shutting down server...")

	cancel()
	wg.Wait()

	log.Println("Server gracefully stopped")
}