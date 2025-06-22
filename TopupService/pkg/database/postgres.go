package database

import (
	"log"
	"os"
	"topup-service/internal/topup"

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

	err = db.AutoMigrate(&topup.Topup{})
	if err != nil {
		log.Fatalf("Failed to auto-migrate database: %v", err)
	}
	
	log.Println("Successfully connected to the database with GORM")
	return db
}