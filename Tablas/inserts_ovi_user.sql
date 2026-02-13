-- Usuario 1: Hombre, datos estándar
INSERT INTO OviUser (dni, name, surname, birthDate, password, email, phone, address, legalGuardian, gender)
VALUES ('12345678A', 'Juan', 'Carmona', '2005-07-08', 'PassSegura1', 'juan.carmona@gmail.com', '600111222', 'Calle Mayor 10', NULL, 'M');

-- Usuario 2: Mujer, apellido largo
INSERT INTO OviUser (dni, name, surname, birthDate, password, email, phone, address, legalGuardian, gender)
VALUES ('87654321B', 'María', 'González', '1985-10-20', 'ClaveDificil24', 'maria.gonz@work.es', '600333444', 'Av. Libertad 5', NULL, 'F');

-- Usuario 3: Menor de edad (con tutor legal)
INSERT INTO OviUser (dni, name, surname, birthDate, password, email, phone, address, legalGuardian, gender)
VALUES ('11223344C', 'Lucas', 'Ramírez', '2015-03-01', 'SoyElMejor123', 'lucas.ram@cole.edu', '700555666', 'Plaza España 3', 'Pedro Ramírez', 'M');

-- Usuario 4: Género 'X' (No binario)
INSERT INTO OviUser (dni, name, surname, birthDate, password, email, phone, address, legalGuardian, gender)
VALUES ('99887766D', 'Alex', 'Sánchez', '1995-12-12', 'NeutralPass99', 'alex.sanc@net.org', '611222333', 'C/ Luna 45', NULL, 'X');

-- Usuario 5: Límite de longitud de contraseña y caracteres en nombre
INSERT INTO OviUser (dni, name, surname, birthDate, password, email, phone, address, legalGuardian, gender)
VALUES ('55667788E', 'Max', 'De La Torre', '2000-01-01', 'SuperLongPassword!', 'max.torre@largo.com', '699888777', 'Camino del Valle', NULL, 'M');