-- H2 DDL Script for Table Definition App

-- Create projects table
CREATE TABLE IF NOT EXISTS projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create table_definitions table
CREATE TABLE IF NOT EXISTS table_definitions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    schema_name VARCHAR(255),
    physical_name VARCHAR(255) NOT NULL,
    logical_name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_project 
        FOREIGN KEY (project_id) 
        REFERENCES projects(id) 
        ON DELETE CASCADE,
    CONSTRAINT uk_project_physical_name UNIQUE (project_id, physical_name)
);

-- Create column_definitions table
CREATE TABLE IF NOT EXISTS column_definitions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_definition_id BIGINT NOT NULL,
    physical_name VARCHAR(255) NOT NULL,
    logical_name VARCHAR(255) NOT NULL,
    data_type VARCHAR(50) NOT NULL,
    length INTEGER,
    precision INTEGER,
    scale INTEGER,
    nullable BOOLEAN NOT NULL DEFAULT TRUE,
    primary_key BOOLEAN NOT NULL DEFAULT FALSE,
    unique_key BOOLEAN NOT NULL DEFAULT FALSE,
    default_value VARCHAR(255),
    description TEXT,
    column_order INTEGER NOT NULL,
    CONSTRAINT fk_table_definition 
        FOREIGN KEY (table_definition_id) 
        REFERENCES table_definitions(id) 
        ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_column_definitions_table_id ON column_definitions(table_definition_id);
CREATE INDEX idx_table_definitions_project_id ON table_definitions(project_id);
CREATE INDEX idx_projects_code ON projects(code);