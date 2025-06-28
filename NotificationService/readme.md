
---

### 🐬 MySQL: `notification_db0` & `notification_db1`

#### ✅ Masuk ke container:

```bash
docker exec -it notification_db0 mysql -u root -psecret notificationdb0
```

atau:

```bash
docker exec -it notification_db1 mysql -u root -psecret notificationdb1
```

#### ✅ Query untuk membuat tabel:

```sql
CREATE TABLE notifications (
  notification_id VARCHAR(255) NOT NULL,
  user_id VARCHAR(255) NOT NULL,
  type VARCHAR(255) NOT NULL,
  message TEXT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (notification_id)
);
```

---

### 🐘 PostgreSQL: `notification_db2`

#### ✅ Masuk ke container:

```bash
docker exec -it notification_db2 psql -U postgres -d notificationdb2
```

#### ✅ Query untuk membuat tabel:

```sql
CREATE TABLE notifications (
  notification_id VARCHAR(255) PRIMARY KEY,
  user_id VARCHAR(255) NOT NULL,
  type VARCHAR(20) NOT NULL,
  message TEXT NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()
);
```

---

### 📝 Catatan

* `NotificationType` pada Java direpresentasikan sebagai `ENUM` di MySQL dan `VARCHAR + CHECK` di PostgreSQL.
* Kolom `created_at` di-generate otomatis saat insert, jadi tidak perlu diisi dari aplikasi.
* Hindari penggunaan enum Java tanpa konversi eksplisit jika memakai PostgreSQL.

---