# --- Build Stage ---
FROM golang:1.23-alpine AS builder

RUN apk add --no-cache build-base librdkafka-dev

WORKDIR /app

COPY go.mod go.sum ./
RUN go mod download

COPY . .

RUN CGO_ENABLED=1 GOOS=linux go build -a -o /walletservice ./cmd/walletservice

# --- Final Stage ---
FROM alpine:latest

RUN apk add --no-cache librdkafka

WORKDIR /root/

COPY --from=builder /walletservice .

EXPOSE 8083

CMD ["./walletservice"]