CREATE TABLE Communication(
	idCommunication 			SERIAL PRIMARY KEY,
	idCandidacy		INTEGER		NOT NULL,
	dateCommunication 	DATE 	DEFAULT CURRENT_DATE NOT NULL,
	information	VARCHAR(500)	NOT NULL,
    transmitterName VARCHAR(50) NOT NULL,
	
	CONSTRAINT fk_Candidacy_id FOREIGN KEY (idCandidacy)
			REFERENCES Candidacy(idCandidacy)
			ON UPDATE CASCADE
			ON DELETE RESTRICT
);