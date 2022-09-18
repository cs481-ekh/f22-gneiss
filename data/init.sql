CREATE DATABASE CAPSTONE;
USE CAPSTONE;
CREATE TABLE users (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    firstname VARCHAR(50) NOT NULL UNIQUE,
    lastname VARCHAR(50) NOT NULL UNIQUE
);
INSERT INTO users (username, password, firstname, lastname) VALUES ('bobbyboo', 'urmom', 'Bobby', 'Boo');