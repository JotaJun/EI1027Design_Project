INSERT INTO AssistanceRequest (
    description, assistantType, gender, city, drivingLicense,
    yearsOfExperience, initialDateRequired, monthsRequired, status, dniOviUser
) VALUES
-- Pendientes (Mayoría)
('Asistencia para movilidad básica', 'PAP', 'M', 'Castellón', true, 2, CURRENT_DATE + INTERVAL '5 days', 6, 'pending', '77777777Q'),
('Apoyo en tareas domésticas', 'PAP', 'F', 'Castellón', false, 1, CURRENT_DATE + INTERVAL '10 days', 12, 'pending', '77777777Q'),
('Acompañamiento universitario', 'PATI', 'M', 'Valencia', true, 3, CURRENT_DATE + INTERVAL '15 days', 9, 'pending', '77777777Q'),
('Ayuda en higiene personal', 'PAP', NULL, 'Vila-real', false, 5, CURRENT_DATE + INTERVAL '2 days', 3, 'pending', '77777777Q'),
('Asistencia en viajes cortos', 'PATI', 'F', 'Burriana', true, 2, CURRENT_DATE + INTERVAL '20 days', 1, 'pending', '77777777Q'),
('Apoyo en gestiones bancarias', 'PAP', 'M', 'Castellón', true, 0, CURRENT_DATE + INTERVAL '7 days', 4, 'pending', '77777777Q'),
('Cuidado nocturno preventivo', 'PAP', 'F', 'Onda', false, 10, CURRENT_DATE + INTERVAL '12 days', 24, 'pending', '77777777Q'),
('Asistencia en actividades de ocio', 'PATI', NULL, 'Benicàssim', true, 1, CURRENT_DATE + INTERVAL '30 days', 3, 'pending', '77777777Q'),
('Apoyo en rehabilitación', 'PAP', 'M', 'Valencia', false, 4, CURRENT_DATE + INTERVAL '8 days', 6, 'pending', '77777777Q'),
('Acompañamiento a citas médicas', 'PAP', 'F', 'Castellón', true, 2, CURRENT_DATE + INTERVAL '1 day', 12, 'pending', '77777777Q'),

-- Aceptadas
('Asistencia para deporte adaptado', 'PATI', 'M', 'Castellón', true, 3, CURRENT_DATE + INTERVAL '15 days', 5, 'accepted', '77777777Q'),
('Apoyo logístico diario', 'PAP', NULL, 'Almassora', false, 1, CURRENT_DATE + INTERVAL '4 days', 2, 'accepted', '77777777Q'),
('Asistencia integral fin de semana', 'PAP', 'F', 'Vila-real', true, 6, CURRENT_DATE + INTERVAL '40 days', 12, 'accepted', '77777777Q'),
('Guía en transporte público', 'PATI', 'M', 'Valencia', false, 0, CURRENT_DATE + INTERVAL '10 days', 6, 'accepted', '77777777Q'),
('Apoyo en lectura y escritura', 'PATI', 'F', 'Castellón', false, 2, CURRENT_DATE + INTERVAL '5 days', 9, 'accepted', '77777777Q'),

-- Rechazadas (Con motivo)
('Asistencia técnica avanzada', 'PAP', 'M', 'Madrid', true, 15, CURRENT_DATE + INTERVAL '60 days', 12, 'rejected', '77777777Q'),
('Apoyo en cocina', 'PAP', 'F', 'Castellón', false, 1, CURRENT_DATE + INTERVAL '5 days', 3, 'rejected', '77777777Q'),
('Acompañamiento a concierto', 'PATI', NULL, 'Barcelona', true, 1, CURRENT_DATE + INTERVAL '10 days', 1, 'rejected', '77777777Q'),

-- Variados adicionales (Pendientes)
('Asistencia en trámites oficiales', 'PAP', 'F', 'Castellón', false, 2, CURRENT_DATE + INTERVAL '14 days', 2, 'pending', '77777777Q'),
('Apoyo en compras semanales', 'PAP', 'M', 'Borriol', true, 1, CURRENT_DATE + INTERVAL '6 days', 12, 'pending', '77777777Q'),
('Acompañamiento en biblioteca', 'PATI', 'F', 'Valencia', false, 0, CURRENT_DATE + INTERVAL '25 days', 4, 'pending', '77777777Q'),
('Asistencia para movilidad reducida', 'PAP', 'M', 'Castellón', true, 8, CURRENT_DATE + INTERVAL '3 days', 6, 'pending', '77777777Q'),
('Apoyo en excursión programada', 'PATI', NULL, 'Peñíscola', true, 2, CURRENT_DATE + INTERVAL '45 days', 1, 'pending', '77777777Q'),
('Asistencia en entorno laboral', 'PAP', 'M', 'Castellón', false, 5, CURRENT_DATE + INTERVAL '11 days', 12, 'pending', '77777777Q'),
('Ayuda con mascota de apoyo', 'PATI', 'F', 'Vila-real', true, 1, CURRENT_DATE + INTERVAL '9 days', 3, 'pending', '77777777Q'),
('Asistencia nocturna fin de semana', 'PAP', 'M', 'Castellón', false, 4, CURRENT_DATE + INTERVAL '18 days', 6, 'pending', '77777777Q'),
('Apoyo en curso de formación', 'PATI', 'F', 'Valencia', false, 2, CURRENT_DATE + INTERVAL '22 days', 5, 'pending', '77777777Q'),
('Asistencia en piscina terapéutica', 'PAP', 'M', 'Castellón', true, 3, CURRENT_DATE + INTERVAL '7 days', 8, 'pending', '77777777Q'),
('Acompañamiento a eventos sociales', 'PATI', NULL, 'Benicàssim', true, 1, CURRENT_DATE + INTERVAL '15 days', 2, 'pending', '77777777Q'),
('Apoyo general diario matutino', 'PAP', 'F', 'Castellón', false, 3, CURRENT_DATE + INTERVAL '4 days', 12, 'pending', '77777777Q');

-- Actualización manual de los motivos de rechazo para los que tienen status 'rejected'
UPDATE AssistanceRequest SET deniedReason = 'Fuera de zona de cobertura habitual' WHERE status = 'rejected' AND city != 'Castellón';
UPDATE AssistanceRequest SET deniedReason = 'Falta de asistentes disponibles para esa fecha' WHERE status = 'rejected' AND city = 'Castellón';