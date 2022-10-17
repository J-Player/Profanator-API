CREATE DATABASE profanator_db;

USE profanator_db;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS Proficiency(
	id UUID DEFAULT uuid_generate_v4(),
	name VARCHAR(255) UNIQUE,
	created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMPTZ,
	version INTEGER,
	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Item(
	id UUID DEFAULT uuid_generate_v4(),
	proficiency VARCHAR(255),
	name VARCHAR(255) NOT NULL UNIQUE,
	qtbyproduction INTEGER NOT NULL CHECK (qtByProduction > 0),
	created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMPTZ,
	version INTEGER,
	PRIMARY KEY (id),
	FOREIGN KEY (proficiency) REFERENCES Proficiency(name) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Ingredient(
	id UUID DEFAULT uuid_generate_v4(),
	product VARCHAR(255) NOT NULL,
	name VARCHAR(255) NOT NULL,
	quantity INTEGER NOT NULL CHECK (quantity > 0),
	created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	updated_at TIMESTAMPTZ,
	version INTEGER,
	PRIMARY KEY (id),
	FOREIGN KEY (product) REFERENCES Item(name) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (name) REFERENCES Item(name) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE (product, name)
);

CREATE TABLE IF NOT EXISTS Profanator_User(
    id UUID DEFAULT uuid_generate_v4(),
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    authorities VARCHAR(255) NOT NULL,
    accountnonexpired BOOLEAN DEFAULT TRUE,
    accountnonlocked BOOLEAN DEFAULT TRUE,
    credentialsnonexpired BOOLEAN DEFAULT TRUE,
    enabled BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id)
);