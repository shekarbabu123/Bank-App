create database banking_system;

use banking_system;

create table user(
    user_id int not null auto_increment,
    user_name varchar(20) not null,
    account_id int,
    password varchar(50) not null,
    PRIMARY KEY (user_id),
    FOREIGN KEY (account_id) REFERENCES account(account_id)
);

create table account(
    account_id int not null auto_increment,
    balance_amount decimal(19,2) not null,
    limit_amount decimal(19,2) not null,
    lien_amount decimal(19,2) not null,
    PRIMARY KEY (account_id)
);
create table transaction(
    transaction_reference_id varchar(40) not null,
    from_account_id int,
    to_account_id int,
    transaction_amount decimal(19,2) not null,
    transaction_date timestamp not null,
    transaction_currency varchar(3) not null,
    PRIMARY KEY (transaction_reference_id),
    FOREIGN KEY (from_account_id) REFERENCES account(account_id),
    FOREIGN KEY (to_account_id) REFERENCES account(account_id)
);

create table hibernate_sequences (
sequence_name varchar(10) not null,
next_val int,
primary key (sequence_name)
);

INSERT INTO hibernate_sequences(sequence_name,next_val) VALUES ('default',1000000);