CREATE TABLE IF NOT EXISTS nodes (
    id VARCHAR(36) PRIMARY KEY,
    value VARCHAR(255) NOT NULL,
    parent_id VARCHAR(36),
    CONSTRAINT fk_parent FOREIGN KEY (parent_id) REFERENCES nodes(id)
);