create table telegram_users
(
    chat_id      varchar(255) primary key,
    email        varchar(255),
    active       boolean,
    check_number integer
)