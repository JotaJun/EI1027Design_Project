-- Inserts para idApRequest = 1 utilizando los DNIs proporcionados
-- Estado: 'talksnotstarted'
INSERT INTO Candidacy (dateLastModified, candidacyStatus, idApRequest, dniPapPati) VALUES
                                                                                       ('2024-01-10', 'talksnotstarted', 1, '40404040D'),
                                                                                       ('2024-01-11', 'talksnotstarted', 1, '50505050E'),
                                                                                       ('2024-01-12', 'talksnotstarted', 1, '60606060F'),
                                                                                       ('2024-01-13', 'talksnotstarted', 1, '70707070G'),
                                                                                       ('2024-01-14', 'talksnotstarted', 1, '80808080K'),
                                                                                       ('2024-01-15', 'talksnotstarted', 1, '90909090W');

-- Estado: 'intalks'
INSERT INTO Candidacy (dateLastModified, candidacyStatus, idApRequest, dniPapPati) VALUES
                                                                                       ('2024-02-01', 'intalks', 1, '40404040D'),
                                                                                       ('2024-02-02', 'intalks', 1, '50505050E'),
                                                                                       ('2024-02-03', 'intalks', 1, '60606060F'),
                                                                                       ('2024-02-04', 'intalks', 1, '70707070G'),
                                                                                       ('2024-02-05', 'intalks', 1, '80808080K');

-- Estado: 'talksended'
INSERT INTO Candidacy (dateLastModified, candidacyStatus, idApRequest, dniPapPati) VALUES
                                                                                       ('2024-03-10', 'talksended', 1, '90909090W'),
                                                                                       ('2024-03-11', 'talksended', 1, '40404040D'),
                                                                                       ('2024-03-12', 'talksended', 1, '50505050E'),
                                                                                       ('2024-03-13', 'talksended', 1, '60606060F'),
                                                                                       ('2024-03-14', 'talksended', 1, '70707070G');

-- Estado: 'contracted'
INSERT INTO Candidacy (dateLastModified, candidacyStatus, idApRequest, dniPapPati) VALUES
                                                                                       ('2024-04-01', 'contracted', 1, '80808080K'),
                                                                                       ('2024-04-02', 'contracted', 1, '90909090W'),
                                                                                       ('2024-04-03', 'contracted', 1, '40404040D'),
                                                                                       ('2024-04-04', 'contracted', 1, '50505050E'),
                                                                                       ('2024-04-05', 'contracted', 1, '60606060F');

-- Algunos adicionales con la fecha por defecto (CURRENT_DATE)
INSERT INTO Candidacy (candidacyStatus, idApRequest, dniPapPati) VALUES
                                                                     ('talksnotstarted', 1, '70707070G'),
                                                                     ('intalks', 1, '80808080K'),
                                                                     ('talksended', 1, '90909090W');