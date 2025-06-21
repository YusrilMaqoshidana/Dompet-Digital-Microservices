package database

import (
	"dompet-digital-microservice/internal/wallet"
	"log"
	"os"

	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

func NewGormDB() *gorm.DB {
	dsn := os.Getenv("DATABASE_URL")
	if dsn == "" {
		log.Fatal("DATABASE_URL environment variable is not set")
	}

	db, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}

	err = db.AutoMigrate(&wallet.Wallet{})
	if err != nil {
		log.Fatalf("Failed to auto-migrate database: %v", err)
	}
	
	log.Println("Successfully connected to the database with GORM")
	return db
}