package report

import (
	"time"
	"github.com/google/uuid"
)

type TransactionLog struct {
	ID              uuid.UUID `gorm:"type:uuid;primary_key;default:gen_random_uuid()" json:"id"`
	UserID          uuid.UUID `gorm:"type:uuid;not null;index" json:"user_id"`
	ReferenceID     uuid.UUID `gorm:"type:uuid;not null" json:"reference_id"`
	TransactionType string    `gorm:"type:log_type;not null" json:"transaction_type"`
	Status          string    `gorm:"type:log_status;not null" json:"status"`
	Amount          float64   `gorm:"type:numeric(15,2);not null" json:"amount"`
	Description     string    `gorm:"type:text" json:"description"`
	Timestamp       time.Time `gorm:"not null" json:"timestamp"`
}

type TopupEvent struct {
	TopupID   uuid.UUID `json:"topup_id"`
	UserID    uuid.UUID `json:"user_id"`
	Amount    float64   `json:"amount"`
	Timestamp time.Time `json:"timestamp"`
	Reason    string    `json:"reason,omitempty"`
}

type TransactionEvent struct {
    TransactionID uuid.UUID `json:"transaction_id"`
    SenderUserID  uuid.UUID `json:"sender_user_id"`
    ReceiverUserID uuid.UUID `json:"receiver_user_id"`
    Amount        float64   `json:"amount"`
    Timestamp     time.Time `json:"timestamp"`
    Notes         string    `json:"notes"`
}