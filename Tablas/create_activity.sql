CREATE TABLE Activity(
    id              SERIAL PRIMARY KEY,     -- Ponemos SERIAL para que se cree automático,
                                            -- NO HACE FALTA PONER CONSTRAINT PRIMARY KEY
    instructorDni   VARCHAR(9)      NOT NULL,    
    title           VARCHAR(50)     NOT NULL,
    typeActivity    VARCHAR(20)     NOT NULL,
    activityDate    DATE            NOT NULL,
    description     TEXT            NOT NULL,
    address         VARCHAR(50)     NOT NULL,
    capacity        INTEGER         NOT NULL,
    sponsor         VARCHAR(30),
    CONSTRAINT fk_activity_instructor FOREIGN KEY (instructorDni)
        REFERENCES Instructor(dni)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT chk_type_activity CHECK(typeActivity in ('dissemination', 'training'))
);