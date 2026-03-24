CREATE TABLE AssistanceRequest(
	idApRequest 					SERIAL PRIMARY KEY,
	creationDate 			DATE	DEFAULT CURRENT_DATE NOT NULL,
	-- PARAMETROS BUSQUEDA
		-- Los que permitan nulos implicará que no importa como restricción
	assistantType			VARCHAR(4)		NOT NULL,
	gender					VARCHAR(1),
	city					VARCHAR(30),
    drivingLicense          BOOLEAN,
	yearsOfExperience		INTEGER,
	initialDateRequired		DATE			NOT NULL,
	monthsRequired			INTEGER			NOT NULL,
	
	-- FIN PARAMETROS BUSQUEDA

    status                  VARCHAR(10) DEFAULT 'pending' NOT NULL,
    deniedReason            VARCHAR(255),
    dniOviUser				VARCHAR(9)		NOT NULL,
	approvedByGuardian		BOOLEAN			DEFAULT FALSE,
	dniLegalGuardian		VARCHAR(9),


	CONSTRAINT chk_assistantType CHECK(assistantType IN ('PAP', 'PATI')),
    CONSTRAINT chk_status CHECK (status IN ('pending', 'accepted', 'rejected')),
	
	CONSTRAINT fk_OVIUser_dni FOREIGN KEY (dniOviUser)
			REFERENCES OviUser(dni)
			ON UPDATE CASCADE
			ON DELETE CASCADE,
	CONSTRAINT fk_legalGuardian_dni FOREIGN KEY (dniLegalGuardian)
        REFERENCES LegalGuardian(dni)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);