package wallet

import (
	"time"

	"github.com/google/uuid"
	"gorm.io/gorm"
)

type Wallet struct {
	ID        uuid.UUID      `gorm:"type:uuid;primary_key;default:gen_random_uuid()" json:"id"`
	UserID    string         `gorm:"column:user_id;type:uuid;not null;uniqueIndex" json:"userId"`
	Balance   float64        `gorm:"type:numeric(15,2);not null;default:0.00" json:"balance"`
	Status    string         `gorm:"type:varchar(20);not null" json:"status"`
	CreatedAt time.Time      `json:"created_at"`
	UpdatedAt time.Time      `json:"updated_at"`
	DeletedAt gorm.DeletedAt `gorm:"index" json:"-"`
}

type UserCreatedEvent struct {
	UserID    string `json:"userId"`
	CreatedAt string `json:"createdAt"`
}

type TopupSuccessEvent struct {
	UserID string  `json:"userId"`
	Amount float64 `json:"amount"`
}

type WalletUpdatedEvent struct {
	UserID    string    `json:"userId"`
	Balance   float64   `json:"new_balance"`
	UpdatedAt time.Time `json:"updated_at"`
}
