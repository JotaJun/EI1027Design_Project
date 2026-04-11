CREATE TABLE PapPati (
    dni                     VARCHAR(9),
    stafftype               VARCHAR(4)     NOT NULL, --reglas de integridad {PAP, PATI}
    drivingLicense          BOOLEAN        NOT NULL,
    initialAvailableDate    DATE,                     
    lastAvailableDate       DATE,                     
    training                VARCHAR(100)    NOT NULL,
    yearsOfExperience         INTEGER         NOT NULL,    
    urlCv   	              VARCHAR(255)    NOT NULL,


    CONSTRAINT 	pk_PapPati PRIMARY KEY (dni),
    CONSTRAINT 	fk_Account_dni FOREIGN KEY (dni)
		REFERENCES Account(dni)
		  ON UPDATE CASCADE
		  ON DELETE CASCADE,
    CONSTRAINT 	chk_stafftype CHECK (stafftype IN ('PAP', 'PATI')),
    CONSTRAINT chk_availableDate CHECK (initialAvailableDate < lastAvailableDate)

);
