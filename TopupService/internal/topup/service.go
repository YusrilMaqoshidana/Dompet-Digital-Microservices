package topup

import (
	"context"
	"encoding/json"
	"log"
	"math/rand"
	"time"

	"github.com/google/uuid"
)

type KafkaProducer interface {
	Publish(ctx context.Context, topic string, key []byte, value []byte) error
}

type Service interface {
	InitiateTopup(ctx context.Context, req CreateTopupRequest) (*Topup, error)
	FindTopupByID(ctx context.Context, topupID uuid.UUID) (*Topup, error)
}

type service struct {
	repo               Repository
	producer           KafkaProducer
	topicSuccess       string
	topicFailed        string
}

func NewService(repo Repository, producer KafkaProducer, topicSuccess, topicFailed string) Service {
	return &service{
		repo:               repo,
		producer:           producer,
		topicSuccess:       topicSuccess,
		topicFailed:        topicFailed,
	}
}

func (s *service) InitiateTopup(ctx context.Context, req CreateTopupRequest) (*Topup, error) {
	newTopup := &Topup{
		ID:     uuid.New(),
		UserID: req.UserID,
		Amount: req.Amount,
		Status: "pending",
	}

	if err := s.repo.CreateTopup(ctx, newTopup); err != nil {
		return nil, err
	}

	go s.processPayment(context.Background(), newTopup)

	return newTopup, nil
}

func (s *service) processPayment(ctx context.Context, topup *Topup) {
	log.Printf("INFO - Starting payment processing for TopupID: %s", topup.ID)

	time.Sleep(time.Duration(2+rand.Intn(3)) * time.Second)

	var finalStatus string
	var reason string
	if rand.Float32() < 0.8 {
		finalStatus = "success"
		log.Printf("INFO - Payment SUCCEEDED for TopupID: %s", topup.ID)
	} else {
		finalStatus = "failed"
		reason = "payment_gateway_declined"
		log.Printf("WARN - Payment FAILED for TopupID: %s", topup.ID)
	}

	if err := s.repo.UpdateTopupStatus(ctx, topup.ID, finalStatus); err != nil {
		log.Printf("ERROR - Failed to update topup status for TopupID %s: %v", topup.ID, err)
		return
	}
	
	s.publishTopupEvent(ctx, topup, finalStatus, reason)
}

func (s *service) publishTopupEvent(ctx context.Context, topup *Topup, status string, reason string) {
	event := TopupEvent{
		TopupID:   topup.ID,
		UserID:    topup.UserID,
		Amount:    topup.Amount,
		Timestamp: time.Now(),
		Reason:    reason,
	}

	eventBytes, err := json.Marshal(event)
	if err != nil {
		log.Printf("ERROR - Failed to marshal TopupEvent: %v", err)
		return
	}

	var topic string
	if status == "success" {
		topic = s.topicSuccess
	} else {
		topic = s.topicFailed
	}

	err = s.producer.Publish(ctx, topic, []byte(topup.UserID.String()), eventBytes)
	if err != nil {
		log.Printf("ERROR - Failed to publish to topic %s: %v", topic, err)
	} else {
		log.Printf("INFO - Published event to topic %s for TopupID %s", topic, topup.ID)
	}
}


func (s *service) FindTopupByID(ctx context.Context, topupID uuid.UUID) (*Topup, error) {
	return s.repo.GetTopupByID(ctx, topupID)
}