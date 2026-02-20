CREATE TABLE AssistanceRequest(
	idApRequest 					SERIAL PRIMARY KEY,
	dateRequest DATE				NOT NULL, 
	userPreferences VARCHAR(200)	NOT NULL,
	dniOviUser	VARCHAR(9)			NOT NULL,
	
	CONSTRAINT fk_OVIUser_dni FOREIGN KEY (dniOviUser)
			REFERENCES OVIUser(dni)
			ON UPDATE CASCADE
			ON DELETE CASCADE
);