---

## 🧾 Inisialisasi Database untuk Topup Service

Sharding dilakukan ke 3 node:

* `topup_db0` (MySQL)
* `topup_db1` (MySQL)
* `topup_db2` (PostgreSQL)

---

### 🐬 Shard 1 – MySQL (`topupdb0`)

```bash
docker exec -it topup_db0 mysql -u root -psecret topupdb0
```

Lalu jalankan query:

```sql
CREATE TABLE topup (
    topup_id VARCHAR(255) PRIMARY KEY,
    external_transaction_id VARCHAR(255) UNIQUE NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    type VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

### 🐬 Shard 2 – MySQL (`topupdb1`)

```bash
docker exec -it topup_db1 mysql -u root -psecret topupdb1
```

Gunakan query yang sama seperti di atas untuk membuat tabel.

---

### 🐘 Shard 3 – PostgreSQL (`topupdb`)

```bash
docker exec -it topup_db2 psql -U postgres -d topupdb2
```

Lalu jalankan:

```sql
CREATE TABLE topup (
    topup_id VARCHAR(255) PRIMARY KEY,
    external_transaction_id VARCHAR(255) UNIQUE NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    amount NUMERIC(19, 4) NOT NULL,
    type VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT now()
);
```

---

## 📌 Catatan

* Gunakan perintah `docker exec -it` untuk masuk ke shell database.
* Data akan di-*shard* berdasarkan strategi tertentu, misalnya berdasarkan `user_id`.

---