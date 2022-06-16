CREATE TABLE addressees
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    addressee_name   VARCHAR(255)          NULL,
    post_code        INT                   NULL,
    settlement       VARCHAR(255)          NULL,
    addressee_number VARCHAR(255)          NULL,
    CONSTRAINT pk_addressees PRIMARY KEY (id)
);