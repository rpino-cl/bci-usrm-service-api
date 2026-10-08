-- =============================================================================
-- schema.sql — Script de creacion
-- =============================================================================
-- H2 en memoria (jdbc:h2:mem:userdb).

-- "users".
CREATE TABLE IF NOT EXISTS users (
    id         VARCHAR(36)   NOT NULL,
    name       VARCHAR(120)  NOT NULL,
    email      VARCHAR(160)  NOT NULL,
    password   VARCHAR(255)  NOT NULL,
    created    TIMESTAMP     NOT NULL,
    modified   TIMESTAMP     NOT NULL,
    last_login TIMESTAMP,
    token      VARCHAR(1000),
    is_active  BOOLEAN       NOT NULL,
    CONSTRAINT PK_USERS PRIMARY KEY (id)
);

-- "El correo ya registrado".
ALTER TABLE users
    ADD CONSTRAINT IF NOT EXISTS UK_USERS_EMAIL UNIQUE (email);

-- phones.
CREATE TABLE IF NOT EXISTS phones (
    id         VARCHAR(36)  NOT NULL,
    number     VARCHAR(30)  NOT NULL,
    citycode   VARCHAR(10),
    contrycode VARCHAR(10),
    user_id    VARCHAR(36)  NOT NULL,
    CONSTRAINT PK_PHONES PRIMARY KEY (id),
    CONSTRAINT FK_PHONES_USER FOREIGN KEY (user_id) REFERENCES users (id)
);
