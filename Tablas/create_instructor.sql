CREATE TABLE Instructor(
	dni 			VARCHAR(9),
	expertise		VARCHAR(30) NOT NULL,
	
	CONSTRAINT pk_Instructor PRIMARY KEY(dni),
	CONSTRAINT fk_Account FOREIGN KEY(dni)
				REFERENCES Account(dni)
				ON UPDATE CASCADE
				ON DELETE CASCADE
);