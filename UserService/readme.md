<!-- Create Table Mysql -->

docker exec -it user_db0 mysql -u root -psecret db_1

docker exec -it user_db1 mysql -u root -psecret db_2

CREATE TABLE users (
    user_id VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    date_registered DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL,
    PRIMARY KEY (user_id)
);


<!-- Create Table Postgresql -->

docker exec -it user_db2 psql -U postgres -d db_3

CREATE TABLE users (
    user_id VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    date_registered TIMESTAMP NOT NULL DEFAULT now(),
    is_active BOOLEAN NOT NULL,
    PRIMARY KEY (user_id)
);
