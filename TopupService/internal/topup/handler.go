package topup

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

type Handler struct {
	service Service
}

func NewHandler(service Service) *Handler {
	return &Handler{service: service}
}

func (h *Handler) RegisterRoutes(router *gin.Engine) {
	router.POST("/topups", h.CreateTopup)
	router.GET("/topups/:topupID", h.GetTopupStatus)
}

func (h *Handler) CreateTopup(c *gin.Context) {
	var req CreateTopupRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	topup, err := h.service.InitiateTopup(c.Request.Context(), req)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to initiate topup process"})
		return
	}

	c.JSON(http.StatusAccepted, topup)
}

func (h *Handler) GetTopupStatus(c *gin.Context) {
	topupIDStr := c.Param("topupID")
	topupID, err := uuid.Parse(topupIDStr)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid Topup ID format"})
		return
	}

	topup, err := h.service.FindTopupByID(c.Request.Context(), topupID)
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			c.JSON(http.StatusNotFound, gin.H{"error": "Topup request not found"})
			return
		}
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Internal server error"})
		return
	}

	c.JSON(http.StatusOK, topup)
}