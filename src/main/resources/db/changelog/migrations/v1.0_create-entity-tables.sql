--liquibase formatted sql

--changeset youngliqui:1
--comment create entity
CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE users
(
    id       BIGINT PRIMARY KEY DEFAULT nextval('users_id_seq'),
    nickname VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    name     VARCHAR(255)        NOT NULL,
    email    VARCHAR(255) UNIQUE NOT NULL,
    role     VARCHAR(50)         NOT NULL
);

CREATE SEQUENCE categories_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE categories
(
    id   BIGINT PRIMARY KEY DEFAULT nextval('categories_id_seq'),
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE SEQUENCE channels_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE channels
(
    id            BIGINT PRIMARY KEY DEFAULT nextval('channels_id_seq'),
    name          VARCHAR(255) UNIQUE NOT NULL,
    description   VARCHAR(500),
    author_id     BIGINT UNIQUE       NOT NULL,
    creation_date DATE,
    language      VARCHAR(50)         NOT NULL,
    avatar        text,
    category_id   BIGINT UNIQUE,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE user_subscriptions
(
    user_id    BIGINT NOT NULL,
    channel_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, channel_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE
);