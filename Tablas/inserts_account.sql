-- !!! NOTA !!!
-- He dividido los inserts según el tipo de cuenta para tener mejor seguimiento

-- Cuentas para Instructores
INSERT INTO Account (dni, name, surname, birthday, password, email, phoneNumber, address, gender) VALUES
('11111111H', 'Joan', 'Pérez García', '1980-05-20', 'passInstructor1', 'jperez@uji.es', '600111222', 'Calle Mayor 5, Castelló', 'M'),
('22222222J', 'Marta', 'Sánchez Miró', '1985-11-10', 'passInstructor2', 'msanchez@uji.es', '611222333', 'Av. Valencia 12, Vila-real', 'F'),
('33333333P', 'Roberto', 'Gómez Llop', '1975-03-15', 'passInstructor3', 'rgomez@uji.es', '622333444', 'Plaza Real 3, Castelló', 'M'),
('44444444L', 'Elena', 'Vidal Beltrán', '1988-07-22', 'passInstructor4', 'evidal@uji.es', '633444555', 'Carrer Enmedio 10, Castelló', 'F'),
('55555555M', 'Carles', 'Marín Soler', '1982-01-30', 'passInstructor5', 'cmarin@uji.es', '644555666', 'Calle Colón 8, Borriana', 'M'),
('66666666N', 'Sofía', 'Castro Ruiz', '1991-09-05', 'passInstructor6', 'scastro@uji.es', '655666777', 'Av. del Mar 20, Castelló', 'F');

-- Cuentas para OviUsers
INSERT INTO Account (dni, name, surname, birthday, password, email, phoneNumber, address, gender) VALUES
('77777777Q', 'Lucas', 'Méndez Pau', '1980-12-01', 'userOviPass1', 'lucas.m@email.com', '666777888', 'Calle Sol 4, Almassora', 'M'),
('88888888R', 'Lucía', 'Ramos Ortiz', '2005-08-25', 'userOviPass2', 'lramos@gmail.com', '677888999', 'Av. Valencia 40, Castelló', 'F'),
('99999999S', 'Hugo', 'Blasco Solá', '2011-11-30', 'userOviPass3', 'hblasco@outlook.es', '688999000', 'Plaza Tetuán 2, Vila-real', 'M'),
('10101010A', 'Ariel', 'Gómez Ferrando', '2006-05-10', 'userOviPass4', 'ariel.g@icloud.com', '699000111', 'Calle Enmedio 15, Castelló', NULL),
('20202020B', 'Pau', 'Vidal Marín', '2009-01-05', 'userOviPass5', 'pvidal@protonmail.com', '601234567', 'Carrer Major 33, Almassora', 'M'),
('30303030C', 'Elena', 'Moltó Ibáñez', '1998-12-12', 'userOviPass6', 'emolto@uji.es', '602345678', 'Calle Herrero 8, Castelló', 'F');

-- Cuentas para PAPati 
INSERT INTO Account (dni, name, surname, birthday, password, email, phoneNumber, address, gender) VALUES
('40404040D', 'Marcos', 'Torres Vila', '1995-02-14', 'passPAP123', 'mtorres@pap.es', '603456789', 'Carrer Escultor, Castelló', 'M'),
('50505050E', 'Sara', 'Crespo Beltrán', '1992-07-27', 'passPAP456', 'screspo@pap.es', '604567890', 'Paseo Ribalta 4, Castelló', 'F'),
('60606060F', 'Iván', 'Castro Gil', '1981-12-25', 'passPAP789', 'ivan.c@pap.es', '605678901', 'Ronda Sud 3, Castelló', 'M'),
('70707070G', 'Julia', 'Navarro Sanz', '1989-04-10', 'passPAP000', 'j.navarro@pap.es', '606789012', 'Calle Nueva 7, Castelló', 'F'),
('80808080K', 'Gonzalo', 'Hernández I.', '1983-06-18', 'passPAP111', 'g.hernandez@pap.es', '607890123', 'Gran Vía 100, Castelló', 'M'),
('90909090W', 'Fatima', 'Ruiz Costa', '1997-11-20', 'passPAP222', 'fatima.r@pap.es', '608901234', 'Calle Real 45, Burriana', 'F');