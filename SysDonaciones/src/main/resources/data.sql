INSERT INTO contribuyente(rut, nombre, email, telefono, tipo) VALUES
('12345678-5', 'José Pérez', 'jperez@123.cl', '+56 9 8877 4433', 'SOCIO')
,('12987654-9', 'Romina Carrasco', 'rcarrasco@hotmail.com', '+56 9 9977 1122', 'SOCIO')
;

INSERT INTO causa(nombre) VALUES
('Donaciones Generales')
,('Donaciones Anónimas')
,('Mensualidades Socios')
;

INSERT INTO configuracion(clave, valor) VALUES
('notificacion-email', 'true')
,('notificacion-sms', 'true')
,('notificacion-app', 'false')
;