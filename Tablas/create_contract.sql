CREATE TABLE Contract(
	idContract 						SERIAL PRIMARY KEY,
	idCandidacy			INTEGER			NOT NULL,
	startDate			DATE 			NOT NULL,
	endDate				DATE			NOT NULL,
	hourlySalary		Numeric(10,2)	NOT NULL,
	schedule			VARCHAR(500)	NOT NULL,
    urlDocument         VARCHAR(255),
	signedByGuardian	BOOLEAN			DEFAULT FALSE,
	dniLegalGuardian	VARCHAR(9),
    status              VARCHAR(10) DEFAULT 'pending' NOT NULL,
    deniedReason        VARCHAR(255),


    CONSTRAINT chk_contract_dates CHECK(startDate < endDate),
    CONSTRAINT chk_status CHECK (status IN ('pending', 'accepted', 'rejected')),


    CONSTRAINT fk_Candidacy_id FOREIGN KEY (idCandidacy)
			REFERENCES Candidacy(idCandidacy)
			ON UPDATE CASCADE
			ON DELETE RESTRICT,
	CONSTRAINT fk_legalGuardian_dni FOREIGN KEY (dniLegalGuardian)
        REFERENCES LegalGuardian(dni)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);