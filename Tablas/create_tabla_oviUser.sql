CREATE TABLE OviUser(
    dni             VARCHAR(9),
    name            VARCHAR(20)     NOT NULL,
    surname         VARCHAR(20)     NOT NULL,
    birthDate       DATE            NOT NULL,
    password        VARCHAR(50)     NOT NULL,
    email           VARCHAR(30)     NOT NULL,
    phone           VARCHAR(9)      NOT NULL,
    address         VARCHAR(50)     NOT NULL,
    legalGuardian   VARCHAR(20),                -- Importante permitir Nulos
    gender          VARCHAR(1)      NOT NULL,
    CONSTRAINT pk_OviUser PRIMARY KEY (dni),
    CONSTRAINT un_OviUser UNIQUE(email),
    CONSTRAINT chk_dni_format CHECK (LENGTH(dni) = 9 AND dni ~ '^[0-9]{8}[A-Z]$'),      -- NOTA: usar ~ para poder usar REGEX
    CONSTRAINT chk_phone_format CHECK (LENGTH(phone) = 9 AND phone ~ '^[0-9]+$'),
    CONSTRAINT chk_gender CHECK (gender IN ('M', 'F', 'X')),
    CONSTRAINT chk_birthdate CHECK (birthDate <= CURRENT_DATE),
    CONSTRAINT chk_email CHECK (email LIKE '%@%.%'),
    CONSTRAINT chk_password CHECK (LENGTH(password) >= 8)   -- Contraseñas mínimas de 8 caracteres
);