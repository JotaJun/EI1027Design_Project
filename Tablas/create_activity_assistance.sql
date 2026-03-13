CREATE TABLE ActivityAssistance(
    idActivityAssistance        SERIAL PRIMARY KEY,
    creationDate                DATE DEFAULT CURRENT_DATE NOT NULL,
    attended                    BOOLEAN DEFAULT FALSE NOT NULL,
    idActivity                  INTEGER NOT NULL,        -- Referencia a un SERIAL
    parentalConsent             BOOLEAN DEFAULT FALSE,   -- Permite nulos si no es menor
    dniOviUser                  VARCHAR(9),
    dniPapPati                  VARCHAR(9),
    dniLegalGuardian            VARCHAR(9),

    CONSTRAINT fk_activity_id FOREIGN KEY (idActivity)
        REFERENCES Activity(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_oviUser_dni FOREIGN KEY (dniOviUser)
        REFERENCES OviUser(dni)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    CONSTRAINT fk_papPati_dni FOREIGN KEY (dniPapPati)
        REFERENCES PapPati(dni)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    CONSTRAINT fk_legalGuardian_dni FOREIGN KEY (dniLegalGuardian)
        REFERENCES LegalGuardian(dni)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);