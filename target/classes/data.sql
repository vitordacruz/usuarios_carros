
insert into usuario (id, birthday, created_at, email, first_name, last_login, last_name, login, password, phone) values (null, '1950-03-20', now(), 'usuario1@email.com', 'Teste', now(), 'Testando', 'usuario1', '$2a$10$aDRDJI8ntJWq8JYunQWsLuZk0F1/PzCCqQn9X2gz7XgdUJAxE2g8K', '55+ (81) 98801-5555');
insert into usuario (id, birthday, created_at, email, first_name, last_login, last_name, login, password, phone) values (null, '1980-05-15', now(), 'usuario2@email.com', 'Teste 02', now(), 'Testando 02', 'usuario2', '$2a$10$aDRDJI8ntJWq8JYunQWsLuZk0F1/PzCCqQn9X2gz7XgdUJAxE2g8K', '55+ (21) 98801-8888');

insert into carro (id, color, license_plate, model, usuario_id, year) values (null, 'Vermelho', 'GPI - 0000', 'Ford Ka', 1, 2018);
insert into carro (id, color, license_plate, model, usuario_id, year) values (null, 'Azul', 'LLL - 0081', 'Sandero', 1, 2020);
insert into carro (id, color, license_plate, model, usuario_id, year) values (null, 'Amarelo', 'AAA - 3388', ' Kombi', 2, 2019);
insert into carro (id, color, license_plate, model, usuario_id, year) values (null, 'Verde', 'CCC - 5552', ' Fusca', 2, 1960);