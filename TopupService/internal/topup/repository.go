package topup

import (
	"context"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

type Repository interface {
	CreateTopup(ctx context.Context, topup *Topup) error
	GetTopupByID(ctx context.Context, topupID uuid.UUID) (*Topup, error)
	UpdateTopupStatus(ctx context.Context, topupID uuid.UUID, status string) error
}

type repository struct {
	db *gorm.DB
}

func NewRepository(db *gorm.DB) Repository {
	return &repository{db: db}
}

func (r *repository) CreateTopup(ctx context.Context, topup *Topup) error {
	return r.db.WithContext(ctx).Create(topup).Error
}

func (r *repository) GetTopupByID(ctx context.Context, topupID uuid.UUID) (*Topup, error) {
	var topup Topup
	err := r.db.WithContext(ctx).First(&topup, "id = ?", topupID).Error
	return &topup, err
}

func (r *repository) UpdateTopupStatus(ctx context.Context, topupID uuid.UUID, status string) error {
	return r.db.WithContext(ctx).Model(&Topup{}).Where("id = ?", topupID).Update("status", status).Error
}