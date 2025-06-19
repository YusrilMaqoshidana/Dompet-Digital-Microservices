package wallet

import (
	"time"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

type Wallet struct {
	ID        uuid.UUID `gorm:"type:uuid;primary_key;default:gen_random_uuid()" json:"id"`
	UserID    uuid.UUID `gorm:"type:uuid;not null;uniqueIndex" json:"user_id"`
	Balance   float64   `gorm:"type:numeric(15,2);not null;default:0.00" json:"balance"`
	Status    string    `gorm:"type:varchar(20);not null" json:"status"`
	CreatedAt time.Time `json:"created_at"`
	UpdatedAt time.Time `json:"updated_at"`
	DeletedAt gorm.DeletedAt `gorm:"index" json:"-"`
}

type UserCreatedEvent struct {
	UserID uuid.UUID `json:"user_id"`
	Email  string    `json:"email"`
}

type TopupSuccessEvent struct {
	UserID uuid.UUID `json:"user_id"`
	Amount float64   `json:"amount"`
}

type WalletUpdatedEvent struct {
	UserID    uuid.UUID `json:"user_id"`
	Balance   float64   `json:"new_balance"`
	UpdatedAt time.Time `json:"updated_at"`
}