CREATE TABLE roles (
    role_id VARCHAR(50) PRIMARY KEY
);

CREATE TABLE users (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role_id VARCHAR(50) NOT NULL,
    banned BOOLEAN,
    FOREIGN KEY(role_id) REFERENCES roles(role_id)
);

CREATE TABLE tasdfa (
    id INT
)

INSERT INTO roles (role_id) VALUES ("user");
INSERT INTO roles (role_id) VALUES ("admin");