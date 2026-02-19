CREATE TABLE PapPati (
    dni         VARCHAR(9),
    stafftype   VARCHAR(10)     NOT NULL, --reglas de integridad {PAP, PATI}
    available   BOOLEAN         NOT NULL,
    training    VARCHAR(100)    NOT NULL,
    experience  VARCHAR(100)    NOT NULL,
    urlCv   	VARCHAR(100)    NOT NULL,
    CONSTRAINT 	pk_PapPati PRIMARY KEY (dni),
    CONSTRAINT 	fk_PapPati_Persona FOREIGN KEY (dni)
		REFERENCES Persona(dni)
		ON UPDATE CASCADE
		ON DELETE CASCADE,
    CONSTRAINT 	chk_stafftype CHECK (stafftype IN ('PAP', 'PATI')),
);
