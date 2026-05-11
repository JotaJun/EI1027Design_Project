CREATE TABLE Communication(
	idCommunication 			SERIAL PRIMARY KEY,
	idCandidacy		INTEGER		NOT NULL,
	dateCommunication 	DATE 	DEFAULT CURRENT_DATE NOT NULL,
	information	VARCHAR(500)	NOT NULL,
    transmitterDni VARCHAR(9) NOT NULL,
	
	CONSTRAINT fk_Candidacy_id FOREIGN KEY (idCandidacy)
			REFERENCES Candidacy(idCandidacy)
			ON UPDATE CASCADE
			ON DELETE RESTRICT,

    CONSTRAINT fk_Account_Transmitter FOREIGN KEY (transmitterDni)
        REFERENCES Account(dni)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);