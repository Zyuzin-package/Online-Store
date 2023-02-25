INSERT INTO users (id, email, name, password, role)
    VALUES (1,'mail@mail.ru','admin','$2a$10$LS/bOqgFcWEcfaEPuCMBYOUqb7WLJDfDF97redD3pE7TrP6O0f0aS','ADMIN') ;

ALTER SEQUENCE user_seq RESTART WITH 3;

INSERT INTO users (id, email, name, password, role)
VALUES (2,'korka@mail.ru','korka','$2a$10$LS/bOqgFcWEcfaEPuCMBYOUqb7WLJDfDF97redD3pE7TrP6O0f0aS','CLIENT') ;

ALTER SEQUENCE user_seq RESTART WITH 3;

INSERT INTO users (id, email, name, password, role)
VALUES (3,'kroka@mail.ru','krok','$2a$10$LS/bOqgFcWEcfaEPuCMBYOUqb7WLJDfDF97redD3pE7TrP6O0f0aS','MANAGER') ;

ALTER SEQUENCE user_seq RESTART WITH 3;