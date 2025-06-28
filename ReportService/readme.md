
---

### 🐬 MySQL: `report_db0` & `report_db1`

#### ✅ Masuk ke container `report_db0`:

```bash
docker exec -it report_db0 mysql -u root -psecret reportdb0
```

#### ✅ Masuk ke container `report_db1`:

```bash
docker exec -it report_db1 mysql -u root -psecret reportdb1
```

#### ✅ Query untuk membuat tabel:

```sql
CREATE TABLE reports (
  report_id VARCHAR(255) NOT NULL,
  transaction_id VARCHAR(255) NOT NULL,
  user_id VARCHAR(255) NOT NULL,
  type VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL,
  amount DECIMAL(19,4) NOT NULL,
  description TEXT,
  transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (report_id)
);
```

---

### 🐘 PostgreSQL: `report_db2`

#### ✅ Masuk ke container:

```bash
docker exec -it report_db2 psql -U postgres -d reportdb2
```

#### ✅ Query untuk membuat tabel:

```sql
CREATE TABLE reports (
  report_id VARCHAR(255) PRIMARY KEY,
  transaction_id VARCHAR(255) NOT NULL,
  user_id VARCHAR(255) NOT NULL,
  type VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL,
  amount NUMERIC(19,4) NOT NULL,
  description TEXT,
  transaction_date TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);
```

---

### 📝 Catatan:

* Kolom `type` dan `status` adalah hasil dari enum `ReportType` dan `TransactionStatus` di model Java.
* Kolom `transaction_date` dibuat otomatis saat insert menggunakan default timestamp.
* Tidak diperlukan pengisian manual untuk waktu `transaction_date` dari aplikasi.

---