CREATE TABLE IF NOT EXISTS user_info (
    username VARCHAR(20) NOT NULL,
    password VARCHAR(50) NOT NULL,
    PRIMARY KEY (username));

CREATE TABLE IF NOT EXISTS chapter  (
    chapter_id  SERIAL,
    name VARCHAR(20),
--     marines_count INT CHECK(marines_count >= 1 AND marines_count <= 1000) NOT NULL,
    marines_count INT NOT NULL,
    PRIMARY KEY(chapter_id));

CREATE TABLE IF NOT EXISTS melee_weapon (
    melee_weapon_id SERIAL,
    melee_weapon VARCHAR(20),
    PRIMARY KEY(melee_weapon_id));

INSERT INTO melee_weapon(melee_weapon)
VALUES ('NULL'),
    ('MANREAPER'),
    ('LIGHTING_CLAW'),
    ('POWER_FIST');

CREATE TABLE IF NOT EXISTS astartes_category (
     astartes_category_id SERIAL,
     astartes_category VARCHAR(20),
     PRIMARY KEY(astartes_category_id));

INSERT INTO astartes_category(astartes_category) VALUES
('NULL'),
('SCOUT'),
('INCEPTOR'),
('TACTICAL'),
('TERMINATOR'),
('APOTHECARY');

CREATE TABLE IF NOT EXISTS coordinates (
   coordinates_id SERIAL,
   x INT NOT NULL CHECK(x >= 0 AND x <= 451),
   y INT NOT NULL CHECK(y >= 0 AND y <= 273),
   PRIMARY KEY(coordinates_id));

CREATE TABLE IF NOT EXISTS weapon (
      weapon_id SERIAL,
      weapon VARCHAR(20),
      PRIMARY KEY(weapon_id));

INSERT INTO weapon(weapon)
VALUES
('NULL'),
('COMBI_FLAMER'),
('FLAMER'),
('INFERNO_PISTOL'),
('MISSILE_LAUNCHER');

CREATE TABLE IF NOT EXISTS space_marine (
    id SERIAL,
    name VARCHAR(20) NOT NULL,
    coordinates_id INT NOT NULL,
    creation_date DATE NOT NULL,
    health INT NOT NULL,
    astartes_category_id INT,
    weapon_id INT,
    melee_weapon_id INT,
    chapter_id INT,
    created_by VARCHAR(20) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (coordinates_id) REFERENCES coordinates(coordinates_id),
    FOREIGN KEY (astartes_category_id) REFERENCES astartes_category(astartes_category_id),
    FOREIGN KEY (weapon_id) REFERENCES weapon(weapon_id),
    FOREIGN KEY (chapter_id) REFERENCES chapter(chapter_id));
