package main

import (
	"log"
	"os"
	"topup-service/internal/topup"
	"topup-service/pkg/config"
	"topup-service/pkg/database"
	"topup-service/pkg/kafka"

	"github.com/gin-gonic/gin"
)

func main() {
	config.LoadConfig()

	db := database.NewGormDB()
	producer := kafka.NewProducer()
	defer producer.Close()

	topupRepo := topup.NewRepository(db)
	topupService := topup.NewService(
		topupRepo,
		producer,
		os.Getenv("KAFKA_TOPIC_TOPUP_SUCCESS"),
		os.Getenv("KAFKA_TOPIC_TOPUP_FAILED"),
	)
	topupHandler := topup.NewHandler(topupService)

	router := gin.Default()
	topupHandler.RegisterRoutes(router)

	appPort := os.Getenv("APP_PORT")
	if appPort == "" {
		appPort = "8082"
	}

	log.Printf("Topup Service is running on port %s", appPort)
	if err := router.Run(":" + appPort); err != nil {
		log.Fatalf("Failed to run server: %v", err)
	}
}