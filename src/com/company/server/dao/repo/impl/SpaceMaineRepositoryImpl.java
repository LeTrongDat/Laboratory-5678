package com.company.server.dao.repo.impl;

import com.company.server.dao.resource.Resource;
import com.company.server.factory.CommandFactory;
import com.company.server.factory.impl.CommandFactoryImpl;
import com.company.server.io.impl.Log;
import com.company.shared.entity.*;
import com.company.server.dao.repo.SpaceMarineRepository;
import com.sun.rowset.CachedRowSetImpl;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

public class SpaceMaineRepositoryImpl implements SpaceMarineRepository {
    private String dbName;
    private Statement stm;

    public SpaceMaineRepositoryImpl() {
        String url = System.getenv("url");
        String host = System.getenv("host");
        String password = System.getenv("password");
        try {
            Connection con = DriverManager.getConnection(url, host, password);
            stm = con.createStatement();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    @Override
    public void loadDatabase(CommandFactory commandFactory) {
        String SQL = String.format("SELECT * FROM %s", dbName);
        try {
            ResultSet rs = stm.executeQuery(SQL);
            for(; rs.next();) commandFactory.add(getSpaceMarine(rs));
        } catch (SQLException e) { handleSQLException(e); }
    }

    @Override
    public void update(SpaceMarine sm) {
        try {
            ResultSet smSet = stm.executeQuery("SELECT * FROM " + dbName + ".space_marine " +
                    "WHERE id = " + sm.getId().toString());
            smSet.next();

            ResultSet chapterSet = stm.executeQuery("SELECT * FROM " + dbName + ".chapter " +
                    "WHERE chapter_id = " + smSet.getInt("chapter_id"));
            chapterSet.next();

            ResultSet coordinatesSet = stm.executeQuery("SELECT * FROM " + dbName + ".coordinates " +
                    "WHERE coordinates_id + " + smSet.getInt("coordinates_id"));
            coordinatesSet.next();

            updateChapter(chapterSet, sm.getChapter());
            updateCoordinates(coordinatesSet, sm.getCoordinates());
            updateSpaceMarine(smSet, sm, chapterSet, coordinatesSet);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    @Override
    public void add(SpaceMarine sm) {
        try {
            insertSpaceMarine(sm);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    @Override
    public void remove(SpaceMarine sm) {

    }

    @Override
    public void register(User user) {
        dbName = "db" + user.getId().toString();
        try {
            new Resource(dbName).run();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // --- private method ---
    private <E extends Enum<E>> E getEnum(ResultSet rs, String name, Class<E> enumClass) throws SQLException {
        String SQL = "SELECT * FROM " + dbName + "." + name +
                "WHERE " + name + "_id = " + ((Integer)rs.getInt(name + "_id")).toString();
        ResultSet resultSet = stm.executeQuery(SQL);
        resultSet.next();
        return Enum.valueOf(enumClass, resultSet.getString("name"));
    }
    SpaceMarine getSpaceMarine(ResultSet rs) throws SQLException {
        Integer id = rs. getInt("id");
        String name = rs.getString("name");
        Coordinates coordinates = null;
        {
            String SQL = "SELECT * FROM " + dbName + ".coordinates " +
                    "WHERE coordinates_id = " + ((Integer)rs.getInt("coordinates_id")).toString();

            ResultSet resultSet = stm.executeQuery(SQL);
            while (resultSet.next()) {
                coordinates = new Coordinates(resultSet.getInt("x"), resultSet.getInt("y"));
            }
        }
        Chapter chapter = null;
        {
            String SQL = "SELECT * FROM " + dbName + ".chapter " +
                    "WHERE chapter_id = " + ((Integer)rs.getInt("chapter_id")).toString();
            ResultSet resultSet = stm.executeQuery(SQL);
            while (resultSet.next()) {
                chapter = new Chapter(resultSet.getString("name"), (long)resultSet.getInt("marines_count"));
            }
        }
        ZonedDateTime creationDate = rs.getDate("creation_date").toInstant().atZone(ZoneId.systemDefault());
        Integer health = rs.getInt("health");
        AstartesCategory category = getEnum(rs, "astartes_category", AstartesCategory.class);
        MeleeWeapon meleeWeapon = getEnum(rs, "melee_weapon", MeleeWeapon.class);
        Weapon weapon = getEnum(rs, "weapon", Weapon.class);

        SpaceMarine sm = new SpaceMarine();
        sm.setId(id);
        sm.setName(name);
        sm.setCoordinates(coordinates);
        sm.setCreationDate(creationDate);
        sm.setHealth(health);
        sm.setCategory(category);
        sm.setWeaponType(weapon);
        sm.setMeleeWeapon(meleeWeapon);
        sm.setChapter(chapter);
        return sm;
    }
    private void updateChapter(ResultSet chapterSet, Chapter chapter) throws SQLException {
        chapterSet.updateString("name", chapter.getName());
        chapterSet.updateInt("marines_count", chapter.getMarinesCount().intValue());
        chapterSet.updateRow();
    }
    private void updateCoordinates(ResultSet coordinatesSet, Coordinates coordinates) throws SQLException {
        coordinatesSet.updateInt("x", coordinates.getX());
        coordinatesSet.updateInt("y", coordinates.getY());
        coordinatesSet.updateRow();
    }
    private void updateSpaceMarine(ResultSet smSet, SpaceMarine sm, ResultSet chSet, ResultSet coorSet) throws SQLException {
        smSet.updateString("name", sm.getName());
        smSet.updateInt("coordinates_id", coorSet.getInt("coordinates_id"));
        smSet.updateInt("health", sm.getHealth());
        smSet.updateInt("astartes_id", sm.getCategory().ordinal());
        smSet.updateInt("weapon_id", sm.getWeaponType().ordinal());
        smSet.updateInt("melee_weapon_id", sm.getMeleeWeapon().ordinal());
        smSet.updateInt("chapter_id", chSet.getInt("chapter_id"));
        smSet.updateRow();
    }
    private void insertSpaceMarine(SpaceMarine sm) throws SQLException {
        ResultSet chapterSet = stm.executeQuery("SELECT *  FROM " + dbName + ".chapter");
        chapterSet.moveToInsertRow();
        updateChapter(chapterSet, sm.getChapter());


        ResultSet coordinatesSet = stm.executeQuery("SELECT * FROM " + dbName + ".coordinates");
        coordinatesSet.moveToInsertRow();
        updateCoordinates(coordinatesSet, sm.getCoordinates());

        ResultSet smSet = stm.executeQuery("SELECT * FROM " + dbName + ".space_marine");
        smSet.moveToInsertRow();
        updateSpaceMarine(smSet, sm, chapterSet, coordinatesSet);
    }
}
