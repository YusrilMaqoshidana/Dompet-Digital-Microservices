package report

import (
	"context"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

type Repository interface {
	CreateLog(ctx context.Context, log *TransactionLog) error
	GetLogsByUserID(ctx context.Context, userID uuid.UUID) ([]TransactionLog, error)
}

type repository struct {
	db *gorm.DB
}

func NewRepository(db *gorm.DB) Repository {
	return &repository{db: db}
}

func (r *repository) CreateLog(ctx context.Context, log *TransactionLog) error {
	return r.db.WithContext(ctx).Create(log).Error
}

func (r *repository) GetLogsByUserID(ctx context.Context, userID uuid.UUID) ([]TransactionLog, error) {
	var logs []TransactionLog
	err := r.db.WithContext(ctx).Where("user_id = ?", userID).Order("timestamp desc").Find(&logs).Error
	return logs, err
}