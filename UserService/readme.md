---

## 🗄️ Inisialisasi Database Sharding (MySQL & PostgreSQL)

Langkah-langkah untuk membuat tabel `users` pada masing-masing shard database:

---

### 🔹 Shard 1 - MySQL (db\_1)

```bash
docker exec -it user_db0 mysql -u root -psecret db_1
```

Lalu jalankan SQL berikut di dalam shell MySQL:

```sql
CREATE TABLE users (
    user_id VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    date_registered DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL,
    PRIMARY KEY (user_id)
);
```

---

### 🔹 Shard 2 - MySQL (db\_2)

```bash
docker exec -it user_db1 mysql -u root -psecret db_2
```

Jalankan SQL yang sama seperti di `db_1`.

---

### 🔹 Shard 3 - PostgreSQL (db\_3)

```bash
docker exec -it user_db2 psql -U postgres -d db_3
```

Lalu jalankan SQL berikut di shell PostgreSQL:

```sql
CREATE TABLE users (
    user_id VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    date_registered TIMESTAMP DEFAULT now(),
    is_active BOOLEAN NOT NULL,
    PRIMARY KEY (user_id)
);
```

---

📌 **Catatan**:

* Kolom `date_registered` diset otomatis pada saat `INSERT`.

