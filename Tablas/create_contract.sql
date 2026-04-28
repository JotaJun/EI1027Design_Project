CREATE TABLE Contract(
	idContract 						SERIAL PRIMARY KEY,
	idCandidacy			INTEGER			NOT NULL,
	startDate			DATE 			NOT NULL,
	endDate				DATE			NOT NULL,
	hourlySalary		Numeric(10,2)	NOT NULL,
	schedule			VARCHAR(500)	NOT NULL,
    urlDocument         VARCHAR(255),

    CONSTRAINT chk_contract_dates CHECK(startDate < endDate),

    CONSTRAINT fk_Candidacy_id FOREIGN KEY (idCandidacy)
			REFERENCES Candidacy(idCandidacy)
			ON UPDATE CASCADE
			ON DELETE RESTRICT
);