---

# Dompet Digital Microservices

Proyek ini merupakan implementasi arsitektur microservices untuk sistem dompet digital menggunakan Spring Boot dan Docker.

## 📦 Langkah Instalasi

### 1. Clone Repositori

Clone repositori ini ke dalam folder lokal Anda:

```bash
git clone https://github.com/YusrilMaqoshidana/Dompet-Digital-Microservices.git
cd Dompet-Digital-Microservices
```

### 2. Build Setiap Service

Masuk ke setiap folder service, lalu jalankan perintah berikut untuk melakukan build dan generate file `.jar`:

```bash
mvn clean package -DskipTests
```

Contoh:

```bash
cd user-service
mvn clean package -DskipTests
cd ../wallet-service
mvn clean package -DskipTests
cd ../topup-service
mvn clean package -DskipTests
# Lanjutkan untuk semua service yang ada
```

### 3. Jalankan Aplikasi dengan Docker Compose

Setelah semua `.jar` berhasil digenerate, kembali ke root folder, lalu jalankan:

```bash
docker compose up -d --build
```

Perintah ini akan membangun dan menjalankan semua container yang telah didefinisikan dalam file `docker-compose.yml`.

---

## 🛠 Teknologi yang Digunakan

* Java 17
* Spring Boot
* Apache Kafka
* MySQL
* PostgreSQL
* ShardingSphere JDBC
* Docker & Docker Compose

---
