package main

import (
	"context"
	"dompet-digital-microservice/internal/wallet"
	"dompet-digital-microservice/pkg/config"
	"dompet-digital-microservice/pkg/database"
	"dompet-digital-microservice/pkg/kafka"
	"log"
	"os"

	"github.com/gin-gonic/gin"
)

func main() {
	config.LoadConfig()

	gormDB := database.NewGormDB()
	sqlDB, err := gormDB.DB()
	if err != nil {
		 log.Fatal("Failed to get underlying sql.DB")
	}
	defer sqlDB.Close()

	producer := kafka.NewProducer()
	defer producer.Close()

	walletRepo := wallet.NewRepository(gormDB)
	walletService := wallet.NewService(walletRepo, producer, os.Getenv("KAFKA_TOPIC_WALLET_UPDATED"))
	walletHandler := wallet.NewHandler(walletService)

	ctx := context.Background()
	go kafka.ConsumeUserCreatedEvents(ctx, walletService)
	go kafka.ConsumeTopupSuccessEvents(ctx, walletService)

	router := gin.Default()
	walletHandler.RegisterRoutes(router)

	appPort := os.Getenv("APP_PORT")
	if appPort == "" {
		appPort = "8081"
	}
	log.Printf("Wallet Service is running on port %s", appPort)
	if err := router.Run(":" + appPort); err != nil {
		log.Fatalf("Failed to run server: %v", err)
	}
}