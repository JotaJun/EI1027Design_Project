CREATE TABLE PapPati (
    dni               VARCHAR(9),
    stafftype         VARCHAR(10)     NOT NULL, --reglas de integridad {PAP, PATI}
    available         BOOLEAN         NOT NULL,
    training          VARCHAR(100)    NOT NULL,
    yearsExperience   INTEGER         NOT NULL,    -- Cambiar UML
    urlCv   	        VARCHAR(100)    NOT NULL,

    CONSTRAINT 	pk_PapPati PRIMARY KEY (dni),
    CONSTRAINT 	fk_Account_dni FOREIGN KEY (dni)
		REFERENCES Account(dni)
		  ON UPDATE CASCADE
		  ON DELETE CASCADE,
    CONSTRAINT 	chk_stafftype CHECK (stafftype IN ('PAP', 'PATI'))
);
