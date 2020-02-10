CREATE TABLE IF NOT EXISTS demo (
    `id` int(11) PRIMARY KEY auto_increment,
    `name` varchar(32) NOT NULL,
    `greeting` varchar(256) DEFAULT NULL
);
