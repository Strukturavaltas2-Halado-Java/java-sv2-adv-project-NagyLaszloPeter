CREATE TABLE parcels
(
    id                   BIGINT AUTO_INCREMENT NOT NULL,
    sender_id            INT                   NULL,
    sending_date_of_time datetime              NULL,
    parcel_type          VARCHAR(255)          NULL,
    addressee_id         BIGINT                NULL,
    CONSTRAINT pk_parcels PRIMARY KEY (id)
);

ALTER TABLE parcels
    ADD CONSTRAINT FK_PARCELS_ON_ADDRESSEE FOREIGN KEY (addressee_id) REFERENCES addressees (id);