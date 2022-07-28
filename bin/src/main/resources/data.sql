delete from lineaspedidos;
delete from ejemplares;
delete from pedidos;
delete from productos;
delete from plataformas;
delete from usuarios;


ALTER TABLE lineaspedidos AUTO_INCREMENT = 0;
ALTER TABLE ejemplares AUTO_INCREMENT = 0;
ALTER TABLE pedidos AUTO_INCREMENT = 0;
ALTER TABLE plataformas AUTO_INCREMENT = 0;
ALTER TABLE productos AUTO_INCREMENT = 0;
ALTER TABLE usuarios AUTO_INCREMENT = 0;



insert into plataformas (nombre, imagen) values ('PC', 'pc.jpg');
insert into plataformas (nombre, imagen) values ('Nintendo Switch', 'nintendoswitch.jpg');
insert into plataformas (nombre, imagen) values ('Xbox One', 'xboxone.jpg');
insert into plataformas (nombre, imagen) values ('PS5', 'ps5.jpg');


insert into productos (disponibilidad, imagen, fechasalida, nombre, precio, idplataforma) values (true, 'EldenRing.jpg', '2022-12-12', 'Elden Ring', 39.99, 1);
insert into productos (disponibilidad, imagen, fechasalida, nombre, precio, idplataforma) values (false, 'DSR.jpg', '2022-12-12', 'DarkSouls Remastered', 19.99, 3);
insert into productos (disponibilidad, imagen, fechasalida, nombre, precio, idplataforma) values (true, 'darkSouls3.jpg', '2022-12-12', 'DarkSouls 3', 29.99, 4);

insert into usuarios (avatar, nombre, apellidos, nick, password, tlf, email, privado, status, fecharegistro) values ('default.jpg' ,'Martin', 'Laviada', 'Nitram', 'admin', '123123456', 'martinlb18@educastur.es', false, true, '2020-12-12');
insert into usuarios (avatar, nombre, apellidos, nick, password, tlf, email, privado, status, fecharegistro) values ('default.jpg', 'Alvaro', 'Jimenez', 'VaroSensei', 'admin', '123202020', 'alvaro@educastur.es', false, true, '2020-12-12');
insert into usuarios (avatar, nombre, apellidos, nick, password, tlf, email, privado, status, fecharegistro) values ('default.jpg', 'Victor', 'Benavides', 'Benav', 'admin', '123202020', 'victos@educastur.es', false, true, '2020-12-12');

insert into ejemplares (clave, fechainserc, idproducto) values ('kdjjYBQuis', '2020-12-12', 1);
insert into ejemplares (clave, fechainserc, idproducto) values ('8vVEqDh8tQ', '2020-12-12', 1);
insert into ejemplares (clave, fechainserc, idproducto) values ('PE8dd6YMjE', '2020-12-12', 1);
insert into ejemplares (clave, fechainserc, idproducto) values ('oC5PK3qd1x', '2020-12-12', 2);
insert into ejemplares (clave, fechainserc, idproducto) values ('v1Iv8nubtX', '2020-12-12', 3);

