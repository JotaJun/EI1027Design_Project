CREATE TABLE Account(
	dni 			VARCHAR(9),
	name			VARCHAR(20) NOT NULL,
	surname			VARCHAR(20) NOT NULL,
	birthday		DATE 		NOT NULL,
	password		VARCHAR(100) NOT NULL,
	email			VARCHAR(30) NOT NULL,
	phoneNumber		VARCHAR(9) 	NOT NULL,
	city 			VARCHAR(30) NOT NULL,	-- Cambiar UML
	street			VARCHAR(30) NOT NULL,	-- Cambiar UML
	zipCode			VARCHAR(5)	NOT NULL,	-- Cambiar UML
	gender			VARCHAR(1), 
	
	CONSTRAINT pk_Account PRIMARY KEY(dni),
	CONSTRAINT un_Account_email UNIQUE(email), 
	CONSTRAINT chk_birthday CHECK(birthday < CURRENT_DATE),
	CONSTRAINT chk_dni CHECK(dni ~ '^[0-9]{8}[A-Z]$'),
	CONSTRAINT chk_phone CHECK(phoneNumber ~ '^[0-9]{9}$'),
	CONSTRAINT chk_zipCode CHECK(zipCode ~ '^[0-9]{5}$'),
	CONSTRAINT chk_email CHECK(email LIKE '%@%.%'),
	CONSTRAINT chk_gender CHECK(gender IN('M','F', 'X')),
	CONSTRAINT chk_password CHECK(LENGTH(password) >=8)
);