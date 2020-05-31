package com.company.server.dao.resource;

import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Resource {
    private String dbName;
    private Statement statement;

    public Resource(String dbName) {
        this.dbName = dbName;
    }
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
        String SQL = "CREATE DATABASE user IF NOT EXISTS ";
        statement.addBatch(SQL);
        SQL = "CREATE TABLE user IF NOT EXISTS ( " +
                "username VARCHAR(20) NOT NULL, " +
                "password VARCHAR(50) NOT NULL," +
                "PRIMARY KEY (username))";
        statement.addBatch(SQL);
    }
    private void createChapter() throws SQLException {
        String SQL = "CREATE TABLE " + dbName + ".chapter IF NOT EXISTS (" +
                "chapter_id INT AUTO INCREMENT, " +
                "name VARCHAR(20), " +
                "marines_count INT CHECK(marines_count >= 1 AND marines_count <= 1000) NOT NULL, " +
                "PRIMARY KEY(chapter_id))";
        statement.addBatch(SQL);
    }
    private void createMeleeWeapon() throws SQLException {
        String SQL = "CREATE TABLE " + dbName + ".melee_weapon IF NOT EXISTS (" +
                "melee_weapon_id INT AUTO INCREMENT, " +
                "melee_weapon VARCHAR(20), " +
                "PRIMARY KEY(melee_weapon_id))";
        statement.addBatch(SQL);
        statement.addBatch("INSERT INTO " + dbName + ".melee_weapon " +
                "VALUES " +
                "('MANREAPER'), " +
                "('LIGHTING_CLAW'), " +
                "('POWER_FIST')" );
    }
    private void createAstartesCategory() throws SQLException {
        String SQL = "CREATE TABLE " + dbName + ".astartes_category IF NOT EXISTS (" +
                "astartes_category_id INT AUTO INCREMENT, " +
                "astartes_category VARCHAR(20), " +
                "PRIMARY KEY(astartes_category_id))";
        statement.addBatch(SQL);
        statement.addBatch("INSERT INTO " + dbName + ".astartes_category " +
                "VALUES " +
                "('SCOUT'), " +
                "('INCEPTOR'), " +
                "('TACTICAL'), " +
                "('TERMINATOR'), " +
                "('APOTHECARY')");
    }
    private void createCoordinates() throws SQLException {
        String SQL = "CREATE TABLE " + dbName + ".coordinates IF NOT EXISTS (" +
                "coordinates_id INT AUTO INCREMENT, " +
                "x INT NOT NULL CHECK(x >= 0 AND x <= 451), " +
                "y INT NOT NULL CHECK(y >= 0 AND y <= 273), " +
                "PRIMARY KEY(coordinates_id))";
        statement.addBatch(SQL);
    }
    private void createWeapon() throws SQLException {
        String SQL = "CREATE TABLE " + dbName + ".weapon IF NOT EXISTS (" +
                "weapon_id INT AUTO INCREMENT, " +
                "weapon VARCHAR(20), " +
                "PRIMARY KEY(weapon_id))";

        statement.addBatch(SQL);
        statement.addBatch("INSERT INTO " + dbName + ".weapon " +
                "VALUES " +
                "('COMBI_FLAMER')," +
                "('FLAMER'), " +
                "('INFERNO_PISTOL')," +
                "('MISSILE_LAUNCHER')");
    }
    private void createSpaceMarine() throws SQLException {
        String createSequence = "CREATE SEQUENCE id IF NOT EXISTS START 1";
        statement.addBatch(createSequence);
        String createSpaceMarine  = "CREATE " + dbName + ".space_marine IF NOT EXISTS (" +
                "id INT, " +
                "name VARCHAR(20) NOT NULL, " +
                "coordinates_id INT NOT NULL, " +
                "creation_date DATETIME NOT NULL, " +
                "health INT NOT NULL," +
                "astartes_category_id INT, " +
                "weapon_id INT, " +
                "melee_weapon_id INT, " +
                "chapter_id INT, " +
                "PRIMARY KEY(id)," +
                "FOREIGN KEY (coordinates_id) REFERENCES " + dbName + ".coordinates(coordinates_id), " +
                "FOREIGN KEY (astartes_category_id) REFERENCES " + dbName + ".astartes_category(astartes_category_id), " +
                "FOREIGN KEY (weapon_id) REFERENCES " + dbName + ".weapon(weapon_id), " +
                "FOREIGN KEY (chapter_id) REFERENCES " + dbName + ".chapter(chapter_id))";
        statement.addBatch(createSpaceMarine);
    }

    public void run() throws SQLException {
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
