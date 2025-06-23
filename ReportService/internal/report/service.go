package report

import (
	"context"
	"fmt"
	"log"

	"github.com/google/uuid"
)

type Service interface {
	ProcessTopupSuccessEvent(ctx context.Context, event TopupEvent) error
	ProcessTopupFailedEvent(ctx context.Context, event TopupEvent) error
	GetUserReport(ctx context.Context, userID uuid.UUID) ([]TransactionLog, error)
}

type service struct {
	repo Repository
}

func NewService(repo Repository) Service {
	return &service{repo: repo}
}

func (s *service) ProcessTopupSuccessEvent(ctx context.Context, event TopupEvent) error {
	log.Printf("INFO - Processing TopupSuccessEvent for UserID: %s", event.UserID)
	logEntry := &TransactionLog{
		UserID:          event.UserID,
		ReferenceID:     event.TopupID,
		TransactionType: "TOPUP",
		Status:          "SUCCESS",
		Amount:          event.Amount,
		Description:     fmt.Sprintf("Top-up of %.2f succeeded.", event.Amount),
		Timestamp:       event.Timestamp,
	}
	return s.repo.CreateLog(ctx, logEntry)
}

func (s *service) ProcessTopupFailedEvent(ctx context.Context, event TopupEvent) error {
	log.Printf("INFO - Processing TopupFailedEvent for UserID: %s", event.UserID)
	logEntry := &TransactionLog{
		UserID:          event.UserID,
		ReferenceID:     event.TopupID,
		TransactionType: "TOPUP",
		Status:          "FAILED",
		Amount:          event.Amount,
		Description:     fmt.Sprintf("Top-up of %.2f failed. Reason: %s", event.Amount, event.Reason),
		Timestamp:       event.Timestamp,
	}
	return s.repo.CreateLog(ctx, logEntry)
}

func (s *service) GetUserReport(ctx context.Context, userID uuid.UUID) ([]TransactionLog, error) {
	return s.repo.GetLogsByUserID(ctx, userID)
}