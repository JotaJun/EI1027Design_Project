CREATE TABLE AssistanceRequest(
	idApRequest 					SERIAL PRIMARY KEY,
	creationDate 			DATE	DEFAULT CURRENT_DATE NOT NULL,
	-- PARAMETROS BUSQUEDA
	-- DE AQUÍ
		-- Los que permitan nulos implicará que no importa como restricción
	assistantType			VARCHAR(4)		NOT NULL,
	gender					VARCHAR(1),
	city					VARCHAR(30),
	yearsExperience			INTEGER,
	specifiedTrainings		VARCHAR(100),
	initialDateRequired		DATE			NOT NULL,
	monthsRequired			INTEGER			NOT NULL,	
	
	-- FIN PARAMETROS BUSQUEDA
	-- HASTA AQUÍ AGREGAR A UML

	dniOviUser				VARCHAR(9)		NOT NULL,
	approvedByGuardian		BOOLEAN			DEFAULT FALSE,
	dniLegalGuardian		VARCHAR(9),


	CONSTRAINT chk_assistantType CHECK(assistantType IN ('PAP', 'PATI')),
	
	CONSTRAINT fk_OVIUser_dni FOREIGN KEY (dniOviUser)
			REFERENCES OviUser(dni)
			ON UPDATE CASCADE
			ON DELETE CASCADE,
	CONSTRAINT fk_legalGuardian_dni FOREIGN KEY (dniLegalGuardian)
        REFERENCES LegalGuardian(dni)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);