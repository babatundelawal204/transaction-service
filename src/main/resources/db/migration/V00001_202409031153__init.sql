CREATE TABLE IF NOT EXISTS accounts (
    id UUID PRIMARY KEY DEFAULT  gen_random_uuid(),
    account_number VARCHAR(10) NOT NULL UNIQUE,
    account_name VARCHAR(255) NOT NULL,
    account_type VARCHAR(50) NOT NULL CHECK (account_type IN ('SAVINGS', 'CURRENT')),
    balance NUMERIC(15, 2) NOT NULL CHECK (balance >= 0),
    currency VARCHAR(3) NOT NULL CHECK (currency ~* '^[A-Z]{3}$'),
    branch_code VARCHAR(5),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    status VARCHAR(15) NOT NULL DEFAULT 'ACTIVE' CHECK(status IN ('ACTIVE', 'PND', 'DEACTIVATED'))
);

INSERT INTO accounts (account_number, account_name, account_type, balance, currency, branch_code, status)
VALUES
('ACC0012345', 'John Doe', 'SAVINGS', 1500.50, 'NGN', 'BR001', 'ACTIVE'),
('ACC0012346', 'Jane Smith', 'CURRENT', 2500.75, 'NGN', 'BR002', 'ACTIVE'),
('ACC0012347', 'Acme Corp', 'CURRENT', 10000.00, 'NGN', 'BR003', 'PND'),
('ACC0012348', 'John and Jane Doe', 'SAVINGS', 5000.00, 'NGN', 'BR001', 'DEACTIVATED'),
('ACC0012349', 'Global Ventures', 'SAVINGS', 7500.25, 'NGN', 'BR004', 'ACTIVE');

CREATE TABLE IF NOT EXISTS transactions(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL,
    currency VARCHAR(3) NOT NULL CHECK (currency ~* '^[A-Z]{3}$'),
    description VARCHAR(1000),
    amount NUMERIC(15,2) NOT NULL CHECK (amount > 0),
    status VARCHAR(15) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'SUCCESSFUL', 'INSUFFICIENT_FUNDS', 'FAILED', 'INVALID_ACCOUNT_NUMBER', 'INVALID_RECIPIENT')),
    status_message VARCHAR(1000),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    is_commission_worthy BOOLEAN,
    commission NUMERIC(15,2) DEFAULT 0 CHECK (amount >= 0),
    category VARCHAR(100) NOT NULL CHECK(category IN ('TRANSFERS', 'BILL PAYMENTS', 'AIRTIME')),
    channel VARCHAR(100) NOT NULL CHECK(channel in ('MOBILE', 'INTERNET', 'USSD')),
    payment_reference VARCHAR(80) NOT NULL UNIQUE,
    fee NUMERIC(15,2) DEFAULT 0,
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE INDEX idx_trans_account_id ON transactions(account_id);
CREATE INDEX idx_trans_status ON transactions(status);
CREATE INDEX idx_trans_reference ON transactions(payment_reference);

CREATE TABLE IF NOT EXISTS transaction_entries(
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL,
    type VARCHAR(10) NOT NULL CHECK(type in ('CREDIT', 'DEBIT')),
    amount NUMERIC(15,2) NOT NULL CHECK (amount > 0),
    transaction_id UUID NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts(id),
    FOREIGN KEY (transaction_id) REFERENCES transactions(id)
);


