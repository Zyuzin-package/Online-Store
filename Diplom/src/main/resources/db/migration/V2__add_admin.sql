INSERT INTO users (id, email, name, password, role)
VALUES (2,'mail@mail.ru','admin','$2a$10$LS/bOqgFcWEcfaEPuCMBYOUqb7WLJDfDF97redD3pE7TrP6O0f0aS','ADMIN') ;

ALTER SEQUENCE user_seq RESTART WITH 3;