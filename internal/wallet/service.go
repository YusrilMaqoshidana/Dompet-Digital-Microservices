package wallet

import (
	"context"
	"encoding/json"
	"log"
	"time"

	"github.com/google/uuid"
)

type KafkaProducer interface {
	Publish(ctx context.Context, topic string, key []byte, value []byte) error
}

type Service interface {
	HandleUserCreated(ctx context.Context, event UserCreatedEvent) error
	HandleTopupSuccess(ctx context.Context, event TopupSuccessEvent) error

	FindWalletByUserID(ctx context.Context, userID uuid.UUID) (*Wallet, error)
}

type service struct {
	repo     Repository
	producer KafkaProducer
	walletUpdatedTopic string
}

func NewService(repo Repository, producer KafkaProducer, walletUpdatedTopic string) Service {
	return &service{
		repo:     repo,
		producer: producer,
		walletUpdatedTopic: walletUpdatedTopic,
	}
}

func (s *service) HandleUserCreated(ctx context.Context, event UserCreatedEvent) error {
	log.Printf("INFO - Handling UserCreatedEvent for UserID: %s", event.UserID)

	_, err := s.repo.GetWalletByUserID(ctx, event.UserID)
	if err == nil {
		log.Printf("WARN - Wallet for UserID %s already exists. Skipping creation.", event.UserID)
		return nil
	}

	newWallet := &Wallet{
		ID:        uuid.New(),
		UserID:    event.UserID,
		Balance:   0,
		Status:    "active",
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	if err := s.repo.CreateWallet(ctx, newWallet); err != nil {
		log.Printf("ERROR - Failed to create wallet for UserID %s: %v", event.UserID, err)
		return err
	}

	log.Printf("INFO - Successfully created wallet for UserID %s", event.UserID)
	return nil
}

func (s *service) HandleTopupSuccess(ctx context.Context, event TopupSuccessEvent) error {
	log.Printf("INFO - Handling TopupSuccessEvent for UserID: %s with amount: %.2f", event.UserID, event.Amount)

	updatedWallet, err := s.repo.CreditBalance(ctx, event.UserID, event.Amount)
	if err != nil {
		log.Printf("ERROR - Failed to credit balance for UserID %s: %v", event.UserID, err)
		return err
	}

	log.Printf("INFO - Successfully credited balance for UserID %s. New balance: %.2f", updatedWallet.UserID, updatedWallet.Balance)

	walletEvent := WalletUpdatedEvent{
		UserID:    updatedWallet.UserID,
		Balance:   updatedWallet.Balance,
		UpdatedAt: updatedWallet.UpdatedAt,
	}
	eventBytes, err := json.Marshal(walletEvent)
	if err != nil {
		log.Printf("ERROR - Failed to marshal WalletUpdatedEvent: %v", err)
		return err
	}

	err = s.producer.Publish(ctx, s.walletUpdatedTopic, []byte(updatedWallet.UserID.String()), eventBytes)
	if err != nil {
		log.Printf("ERROR - Failed to publish WalletUpdatedEvent: %v", err)
	}

	return nil
}

func (s *service) FindWalletByUserID(ctx context.Context, userID uuid.UUID) (*Wallet, error) {
	return s.repo.GetWalletByUserID(ctx, userID)
}