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
    banned BOOLEAN default 0,
    FOREIGN KEY(role_id) REFERENCES roles(role_id)
);

INSERT INTO roles (role_id) VALUES ("user");
INSERT INTO roles (role_id) VALUES ("admin");

-- Create root user with admin privileges. BAN AS SOON AS OTHER ADMIN IS SET UP
-- email: admin
-- password: password
INSERT INTO users (email, password, first_name, last_name, role_id) VALUES ('admin', '$2y$10$l0ddq4xTo/FE69xQRNQReOmfyvTiEaP/LD3f98GyWG3EmjSze50se', 'first', 'last', 'admin')