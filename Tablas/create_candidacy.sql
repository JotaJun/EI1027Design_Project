CREATE TABLE Candidacy(
    idCandidacy         SERIAL PRIMARY KEY,
    dateLastModified    DATE DEFAULT CURRENT_DATE NOT NULL,
    candidacyStatus     VARCHAR(10) DEFAULT 'pending' NOT NULL,
    idApRequest         INTEGER NOT NULL,        -- Referencia a un SERIAL
    dniPapPati          VARCHAR(9)  NOT NULL,

    CONSTRAINT fk_ap_request_id FOREIGN KEY (idApRequest)
        REFERENCES AssistanceRequest(idApRequest)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_papPati_dni FOREIGN KEY (dniPapPati)
        REFERENCES PapPati(dni)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT chk_status CHECK(candidacyStatus IN ('pending, accepted, rejected'))
);