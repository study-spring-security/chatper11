CREATE TABLE IF NOT EXISTS `spring`.`user`
(
    `username` VARCHAR(45) NULL unique ,
    `password` TEXT        NULL
);

CREATE TABLE IF NOT EXISTS `spring`.`otp`
(
    `username` VARCHAR(45) NOT NULL unique ,
    `code`     VARCHAR(45) NULL
);