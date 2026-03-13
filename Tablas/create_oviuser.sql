CREATE TABLE OviUser(
	dni 				VARCHAR(9),
	dniLegalGuardian	VARCHAR(9),
	
	CONSTRAINT pk_OVIUser PRIMARY KEY(dni),
	CONSTRAINT fk_Account FOREIGN KEY(dni)
				REFERENCES Account(dni)
				ON UPDATE CASCADE
				ON DELETE CASCADE,
	CONSTRAINT fk_legalGuardian_dni FOREIGN KEY (dniLegalGuardian)
        REFERENCES LegalGuardian(dni)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);