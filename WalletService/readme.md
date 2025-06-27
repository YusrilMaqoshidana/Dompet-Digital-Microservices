---

## 💳 Inisialisasi Database Sharding untuk Wallet Service

Langkah-langkah pembuatan tabel `wallets` pada masing-masing node database MySQL dan PostgreSQL:

---

### 🔹 Shard 1 - MySQL (walletdb0)

```bash
docker exec -it wallet_db0 mysql -u root -psecret walletdb0
```

Lalu di dalam shell MySQL:

```sql
CREATE TABLE wallets (
    wallet_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    account_number VARCHAR(255) NOT NULL,
    balance DECIMAL(19, 4) NOT NULL DEFAULT 0.00,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (wallet_id)
);
```

---

### 🔹 Shard 2 - MySQL (walletdb1)

```bash
docker exec -it wallet_db1 mysql -u root -psecret walletdb1
```

Lalu jalankan SQL yang sama seperti di `walletdb0`.

---

### 🔹 Shard 3 - PostgreSQL (walletdb2)

```bash
docker exec -it wallet_db2 psql -U postgres -d walletdb2
```

Lalu jalankan perintah berikut:

```sql
CREATE TABLE wallets (
    wallet_id UUID NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    account_number VARCHAR(255) NOT NULL,
    balance NUMERIC(19, 4) NOT NULL DEFAULT 0.00,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    PRIMARY KEY (wallet_id)
);

CREATE INDEX idx_wallets_user_id ON wallets(user_id);
```

---

📌 **Catatan**:

* Field `created_at` dan `updated_at` otomatis diisi saat `INSERT`.
* Di MySQL, `updated_at` juga otomatis diperbarui saat `UPDATE`.
* PostgreSQL perlu trigger manual jika ingin `updated_at` otomatis saat update.

---
