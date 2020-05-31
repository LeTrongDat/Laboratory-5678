package com.company.resource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Resource {
    private Statement statement;

    private void setProperties() throws SQLException {
        createUser();
        createMeleeWeapon();
        createAstartesCategory();
        createWeapon();
        createChapter();
        createCoordinates();
        createSpaceMarine();
    }
    private void createUser() throws SQLException {
        String SQL = "CREATE TABLE IF NOT EXISTS user_info ( " +
                "username VARCHAR(20) NOT NULL, " +
                "password VARCHAR(50) NOT NULL," +
                "PRIMARY KEY (username))";
        statement.addBatch(SQL);
    }
    private void createChapter() throws SQLException {
        String SQL = "CREATE TABLE IF NOT EXISTS chapter  (" +
                "chapter_id  SERIAL, " +
                "name VARCHAR(20), " +
                "marines_count INT CHECK(marines_count >= 1 AND marines_count <= 1000) NOT NULL, " +
                "PRIMARY KEY(chapter_id))";
        statement.addBatch(SQL);
    }
    private void createMeleeWeapon() throws SQLException {
        String SQL = "CREATE TABLE IF NOT EXISTS melee_weapon (" +
                "melee_weapon_id SERIAL, " +
                "melee_weapon VARCHAR(20), " +
                "PRIMARY KEY(melee_weapon_id))";
        statement.addBatch(SQL);
        statement.addBatch("INSERT INTO melee_weapon(melee_weapon) " +
                "VALUES " +
                "('null'), " +
                "('MANREAPER'), " +
                "('LIGHTING_CLAW'), " +
                "('POWER_FIST')" );
    }
    private void createAstartesCategory() throws SQLException {
        String SQL = "CREATE TABLE IF NOT EXISTS astartes_category (" +
                "astartes_category_id SERIAL, " +
                "astartes_category VARCHAR(20), " +
                "PRIMARY KEY(astartes_category_id))";
        statement.addBatch(SQL);
        statement.addBatch("INSERT INTO astartes_category(astartes_category) " +
                "VALUES " +
                "('null'), " +
                "('SCOUT'), " +
                "('INCEPTOR'), " +
                "('TACTICAL'), " +
                "('TERMINATOR'), " +
                "('APOTHECARY')");
    }
    private void createCoordinates() throws SQLException {
        String SQL = "CREATE TABLE IF NOT EXISTS coordinates (" +
                "coordinates_id SERIAL, " +
                "x INT NOT NULL CHECK(x >= 0 AND x <= 451), " +
                "y INT NOT NULL CHECK(y >= 0 AND y <= 273), " +
                "PRIMARY KEY(coordinates_id))";
        statement.addBatch(SQL);
    }
    private void createWeapon() throws SQLException {
        String SQL = "CREATE TABLE IF NOT EXISTS weapon (" +
                "weapon_id SERIAL, " +
                "weapon VARCHAR(20), " +
                "PRIMARY KEY(weapon_id))";

        statement.addBatch(SQL);
        statement.addBatch("INSERT INTO weapon(weapon) " +
                "VALUES " +
                "('null'), " +
                "('COMBI_FLAMER')," +
                "('FLAMER'), " +
                "('INFERNO_PISTOL')," +
                "('MISSILE_LAUNCHER')");
    }
    private void createSpaceMarine() throws SQLException {
        String createSpaceMarine  = "CREATE TABLE IF NOT EXISTS space_marine (" +
                "id SERIAL, " +
                "name VARCHAR(20) NOT NULL, " +
                "coordinates_id INT NOT NULL, " +
                "creation_date DATE NOT NULL, " +
                "health INT NOT NULL," +
                "astartes_category_id INT, " +
                "weapon_id INT, " +
                "melee_weapon_id INT, " +
                "chapter_id INT, " +
                "created_by VARCHAR(20) NOT NULL, " +
                "PRIMARY KEY(id)," +
                "FOREIGN KEY (coordinates_id) REFERENCES coordinates(coordinates_id), " +
                "FOREIGN KEY (astartes_category_id) REFERENCES astartes_category(astartes_category_id), " +
                "FOREIGN KEY (weapon_id) REFERENCES weapon(weapon_id), " +
                "FOREIGN KEY (chapter_id) REFERENCES chapter(chapter_id))";
        statement.addBatch(createSpaceMarine);
    }

    public void run() throws SQLException {
        System.out.println(System.getenv("url"));
        Connection con = DriverManager.getConnection(System.getenv("url"),
                System.getenv("host"),
                System.getenv("password"));
        con.setAutoCommit(false);
        statement = con.createStatement();
        setProperties();
        statement.executeBatch();
        con.commit();
    }
}
