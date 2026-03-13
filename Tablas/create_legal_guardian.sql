CREATE TABLE LegalGuardian(
	dni 			VARCHAR(9),
    signatureCode   VARCHAR(100),
	
	CONSTRAINT pk_LegalGuardian PRIMARY KEY(dni),
	CONSTRAINT fk_Account FOREIGN KEY(dni)
				REFERENCES Account(dni)
				ON UPDATE CASCADE
				ON DELETE CASCADE
);