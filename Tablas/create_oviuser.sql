CREATE TABLE OviUser(
	dni 			VARCHAR(9),
	legalGuardian	VARCHAR(20),
	
	CONSTRAINT pk_OVIUser PRIMARY KEY(dni),
	CONSTRAINT fk_Account FOREIGN KEY(dni)
				REFERENCES Account(dni)
				ON UPDATE CASCADE
				ON DELETE CASCADE
);