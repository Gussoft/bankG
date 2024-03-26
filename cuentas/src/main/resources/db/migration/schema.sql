CREATE TABLE IF NOT EXISTS bankg.account2 (
    id BIGSERIAL PRIMARY KEY,
    id_customer BIGINT NOT NULL,
    account_number VARCHAR(20) NOT NULL,
    account_type VARCHAR(30) NOT NULL,
    initial_balance NUMERIC(19, 2) NOT NULL,
    status VARCHAR(20) NOT NULL
    );

CREATE TABLE IF NOT EXISTS bankg.transaction2 (
    id BIGSERIAL PRIMARY KEY,
    id_account BIGINT NOT NULL,
    create_at TIMESTAMP NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    balance NUMERIC(19, 2) NOT NULL
    );

CREATE TABLE IF NOT EXISTS bankg.customer (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    age INTEGER NOT NULL,
    dni VARCHAR(8) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(12) NOT NULL,
    password VARCHAR(50) NOT NULL,
    state VARCHAR(20) NOT NULL
    );