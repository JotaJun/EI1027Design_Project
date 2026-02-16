-- !!! NOTA !!!
-- Van después de la tabla Instructor (depende de esta)

-- Actividad 1: Formación (Training) - Curso básico para PAPs
INSERT INTO Activity (instructorDni, title, typeActivity, activityDate, description, address, capacity, sponsor)
VALUES (
    '11111111H',                            
    'Curs Iniciació Assistència Personal',  
    'training',                             
    '2025-10-15',
    'Curs bàsic sobre la filosofia de Vida Independent i les tasques principals d''un assistent personal.',
    'Aula 101, FCT, UJI',                   
    20,
    'Ajuntament de Castelló'                
);

-- Actividad 2: Divulgación (Dissemination) - Proyecto VICOOP (mencionado en el PDF)
INSERT INTO Activity (instructorDni, title, typeActivity, activityDate, description, address, capacity, sponsor)
VALUES (
    '22222222J',                            
    'Presentació Projecte VICOOP',
    'dissemination',
    '2025-11-20',
    'Jornada de portes obertes per explicar el projecte d''habitatge col·laboratiu i inclusiu.',
    'Teatre del Raval, Castelló',
    100,
    'Generalitat Valenciana'
);

-- Actividad 3: Formación (Training) - Taller específico con Sponsor NULL
INSERT INTO Activity (instructorDni, title, typeActivity, activityDate, description, address, capacity, sponsor)
VALUES (
    '11111111H',                            
    'Taller: Mobilització Segura',
    'training',
    '2026-02-10',
    'Sessió pràctica per aprendre a realitzar transferències físiques sense risc de lesió per a l''usuari o l''assistent.',
    'Gimnàs UJI, Zona Sud',
    15,
    NULL                                    -- Sponsor es opcional, probamos NULL
);

-- Actividad 4: Divulgación (Dissemination) - Charla Derechos
INSERT INTO Activity (instructorDni, title, typeActivity, activityDate, description, address, capacity, sponsor)
VALUES (
    '33333333P',                            
    'Drets i Diversitat Funcional',
    'dissemination',
    '2026-03-05',
    'Taula rodona sobre la convenció de l''ONU i l''estat actual dels drets de les persones amb diversitat funcional.',
    'Menador Espai Cultural',
    80,
    'Diputació de Castelló'
);

-- Actividad 5: Formación (Training) - Curso Avanzado
INSERT INTO Activity (instructorDni, title, typeActivity, activityDate, description, address, capacity, sponsor)
VALUES (
    '22222222J',                            
    'Eines Digitals per a la Gestió',
    'training',
    '2026-04-12',
    'Formació sobre l''ús de l''aplicació SgOVI i altres eines digitals per a la gestió diària de l''assistència.',
    'Lab. Informàtica 3, ESTCE',
    25,
    'Universitat Jaume I'
);