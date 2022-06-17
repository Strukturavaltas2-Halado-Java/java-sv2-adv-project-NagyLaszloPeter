CREATE TABLE addressees
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    addressee_name   VARCHAR(100)          NULL,
    post_code        INT                   NULL,
    settlement       VARCHAR(50)          NULL,
    addressee_number VARCHAR(25)          NULL,
    CONSTRAINT pk_addressees PRIMARY KEY (id)
);