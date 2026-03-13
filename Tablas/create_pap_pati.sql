CREATE TABLE PapPati (
    dni                     VARCHAR(9),
    status                  VARCHAR(10) DEFAULT 'pending' NOT NULL,
    stafftype               VARCHAR(4)     NOT NULL, --reglas de integridad {PAP, PATI}
    initialAvailableDate    DATE,                     -- Agregar UML, Si NULO --> NO DISPONIBLE
    lastAvailableDate       DATE,                     -- Agregar UML, Si NULO --> NO DISPONIBLE
    training                VARCHAR(100)    NOT NULL,
    yearsOfperience         INTEGER         NOT NULL,    -- Cambiar UML
    urlCv   	              VARCHAR(100)    NOT NULL,


    CONSTRAINT 	pk_PapPati PRIMARY KEY (dni),
    CONSTRAINT 	fk_Account_dni FOREIGN KEY (dni)
		REFERENCES Account(dni)
		  ON UPDATE CASCADE
		  ON DELETE CASCADE,
    CONSTRAINT 	chk_stafftype CHECK (stafftype IN ('PAP', 'PATI')),
    CONSTRAINT chk_availableDate CHECK (initialAvailableDate < lastAvailableDate),
    CONSTRAINT chk_status CHECK (status IN ('pending', 'accepted', 'rejected'))
);
