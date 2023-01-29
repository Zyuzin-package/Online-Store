INSERT INTO users (id,archive, email, name, password, role, bucket_id)
VALUES (2,false,'mail@mail.ru','admin','$2a$10$LS/bOqgFcWEcfaEPuCMBYOUqb7WLJDfDF97redD3pE7TrP6O0f0aS','ADMIN',null);

ALTER SEQUENCE user_seq RESTART WITH 3;