CREATE TABLE AssistanceRequest(
	idApRequest 					SERIAL PRIMARY KEY,
	dateRequest DATE				NOT NULL,
	-- PARAMETROS BUSQUEDA
	-- DE AQUÍ
		-- Los que permitan nulos implicará que no importa como restricción
	assistantType		VARCHAR(4)		NOT NULL,
	gender				VARCHAR(1),
	city				VARCHAR(30),
	yearsExperience		INTEGER,
	specifiedTrainings	VARCHAR(100),
		
	
	-- FIN PARAMETROS BUSQUEDA
	monthsRequired		INTEGER			NOT NULL,	-- Será visible al PAP/PATI en su bandeja de conversaciones que le han iniciado
	startingDate		DATE			NOT NULL,	-- Esta fecha será visible al PAP/PATI en su bandeja de conversaciones que le han iniciado
	-- HASTA AQUÍ AGREGAR A UML
	dniOviUser	VARCHAR(9)			NOT NULL,

	CONSTRAINT chk_assistantType CHECK(assistantType IN ('PAP', 'PATI')),
	
	CONSTRAINT fk_OVIUser_dni FOREIGN KEY (dniOviUser)
			REFERENCES OviUser(dni)
			ON UPDATE CASCADE
			ON DELETE CASCADE
);