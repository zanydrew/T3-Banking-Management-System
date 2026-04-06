CREATE TABLE users (
                       `user_id` INT AUTO_INCREMENT PRIMARY KEY,
                       `username` VARCHAR(50) NOT NULL,
                       `password` VARCHAR(255) NOT NULL,
                       `full_name` VARCHAR(100) NOT NULL,
                       `phone` VARCHAR(20) UNIQUE NOT NULL,
                       `dOB` VARCHAR(20) NOT NULL,
                       `role` ENUM('CUSTOMER','MANAGER') NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO users (user_id, username, password, full_name, phone, dOB, role) VALUES
                                                                                 (1, 'jdoe88', 'P@ssw0rd2026!', 'John Doe', '0123456781', '15-01-1995', 'CUSTOMER'),
                                                                                 (2, 'asmith_mgr', 'Secure#Mgr1', 'Alice Smith', '0123456782', '22-03-1992', 'MANAGER'),
                                                                                 (3, 'michaelb', 'M1ch@el#B', 'Michael Brown', '0123456783', '10-07-1988', 'CUSTOMER'),
                                                                                 (4, 'emilyd', 'Em1ly$D@vis', 'Emily Davis', '0123456784', '05-11-1996', 'CUSTOMER'),
                                                                                 (5, 'davidw', 'D@v1d#Wils0n', 'David Wilson', '0123456785', '18-09-1990', 'CUSTOMER'),
                                                                                 (6, 'saraht', 'S@r@h!Tayl0r', 'Sarah Taylor', '0123456786', '30-06-1993', 'CUSTOMER'),
                                                                                 (7, 'chrisand', 'Chr1s@And#7', 'Chris Anderson', '0123456787', '12-12-1987', 'CUSTOMER'),
                                                                                 (8, 'oliviat', 'Ol1v1@#Thos', 'Olivia Thomas', '0123456788', '25-04-1998', 'CUSTOMER'),
                                                                                 (9, 'danielj', 'D@n1el!Jack', 'Daniel Jackson', '0123456789', '14-08-1991', 'CUSTOMER'),
                                                                                 (10, 'sophiaw', 'S0ph1@#White', 'Sophia White', '0123456790', '02-02-1994', 'MANAGER'),
                                                                                 (11, 'thor_o', 'Thund3r!God', 'Thor Odinson', '0332221111', '05-05-0965', 'CUSTOMER'),
                                                                                 (12, 'bruce_b', 'Hulk$Smash1', 'Bruce Banner', '0990001111', '18-12-1978', 'MANAGER'),
                                                                                 (13, 'scott_l', 'AntM@n!Small', 'Scott Lang', '0778889999', '25-03-1980', 'CUSTOMER'),
                                                                                 (14, 'hope_v', 'W@sp#Flight', 'Hope van Dyne', '0114445555', '14-10-1983', 'CUSTOMER'),
                                                                                 (15, 'sam_w', 'F@lcon$Fly', 'Sam Wilson', '0667778888', '20-09-1982', 'CUSTOMER'),
                                                                                 (16, 'bucky_b', 'Wint3r$Sold', 'Bucky Barnes', '0556667777', '10-03-1917', 'CUSTOMER'),
                                                                                 (17, 't_challa', 'P@nther#Kng', 'T\'Challa', '0445556666', '20-11-1985', 'MANAGER'),
                                                                                (18, 'carol_d', 'M@rvel!Star', 'Carol Diver', '0334445555', '24-04-1970', 'CUSTOMER'),
                                                                                (19, 'stephen_s', 'DrStr@nge#1', 'Stephen Strange', '0223334444', '11-11-1976', 'CUSTOMER'),
                                                                                (20, 'nick_f', 'Dir3ct0r!SH', 'Nick Fury', '0000000001', '21-12-1950', 'MANAGER');

