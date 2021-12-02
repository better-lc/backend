-- +goose Up
-- +goose StatementBegin

CREATE TABLE users(
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    discord_id VARCHAR(50) DEFAULT NULL,
    `rank` INT NOT NULL DEFAULT 0,
    flags INT NOT NULL DEFAULT 0,
    cape_id INT DEFAULT NULL,
    hat_id INT DEFAULT NULL,
    mask_id INT DEFAULT NULL,
    bandanna_id INT DEFAULT NULL,
    tie_id INT DEFAULT NULL,
    wings_id INT DEFAULT NULL,
    backpack_id INT DEFAULT NULL
);

-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin

DROP TABLE users;

-- +goose StatementEnd
