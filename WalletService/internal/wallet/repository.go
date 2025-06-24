package wallet

import (
	"context"
	"errors"

	"gorm.io/gorm"
	"gorm.io/gorm/clause"
)

type Repository interface {
	CreateWallet(ctx context.Context, wallet *Wallet) error
	GetWalletByUserID(ctx context.Context, userID string) (*Wallet, error)
	CreditBalance(ctx context.Context, userID string, amount float64) (*Wallet, error)
}

type repository struct {
	db *gorm.DB
}

func NewRepository(db *gorm.DB) Repository {
	return &repository{db: db}
}

func (r *repository) CreateWallet(ctx context.Context, wallet *Wallet) error {
	result := r.db.WithContext(ctx).Create(wallet)
	return result.Error
}

func (r *repository) GetWalletByUserID(ctx context.Context, userID string) (*Wallet, error) {
	var wallet Wallet
	result := r.db.WithContext(ctx).Where("user_id = ?", userID).First(&wallet)

	if result.Error != nil {
		if errors.Is(result.Error, gorm.ErrRecordNotFound) {
			return nil, gorm.ErrRecordNotFound
		}
		return nil, result.Error
	}
	return &wallet, nil
}

func (r *repository) CreditBalance(ctx context.Context, userID string, amount float64) (*Wallet, error) {
	result := r.db.WithContext(ctx).Model(&Wallet{}).
		Clauses(clause.Returning{}).
		Where("user_id = ?", userID).
		Update("balance", gorm.Expr("balance + ?", amount))

	if result.Error != nil {
		return nil, result.Error
	}
	if result.RowsAffected == 0 {
		return nil, gorm.ErrRecordNotFound
	}

	return r.GetWalletByUserID(ctx, userID)
}
