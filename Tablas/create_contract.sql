CREATE TABLE Contract(
	idContract 						SERIAL PRIMARY KEY,
	idCandidacy			INTEGER			NOT NULL,
	startDate			DATE 			NOT NULL,
	endDate				DATE			NOT NULL,
	hourlySalary		Numeric(10,2)	NOT NULL,
	schedule			VARCHAR(500)	NOT NULL,
    urlDocument         VARCHAR(500)    NOT NULL,   -- Donde se almacenará el documento
    creationDate        DATE            DEFAULT CURRENT_DATE NOT NULL,

    CONSTRAINT chk_contract_dates CHECK(startDate < endDate),
    CONSTRAINT chk_salary CHECK(hourlySalary > 0.0),

    CONSTRAINT fk_Candidacy_id FOREIGN KEY (idCandidacy)
			REFERENCES Candidacy(idCandidacy)
			ON UPDATE CASCADE
			ON DELETE RESTRICT
);