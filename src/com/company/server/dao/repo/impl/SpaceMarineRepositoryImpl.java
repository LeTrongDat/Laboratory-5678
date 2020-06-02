package com.company.server.dao.repo.impl;

import com.company.server.factory.CommandFactory;
import com.company.shared.entity.*;
import com.company.server.dao.repo.SpaceMarineRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SpaceMarineRepositoryImpl implements SpaceMarineRepository {
    private String username;

    public Statement getStm() {
        String url = System.getenv("url");
        String host = System.getenv("host");
        String password = System.getenv("password");
        try {
            Connection con = DriverManager.getConnection(url, host, password);
            return con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null;
    }

    @Override
    public void loadDatabase(CommandFactory commandFactory) {
        String SQL = String.format("SELECT * FROM space_marine WHERE created_by = '%s'", username);
        try {
            ResultSet rs = getStm().executeQuery(SQL);
            while (rs.next()) commandFactory.add(getSpaceMarine(rs));
        } catch (SQLException e) { handleSQLException(e); }
    }

    @Override
    public void update(SpaceMarine sm) {
        try {
            ResultSet smSet = getStm().executeQuery("SELECT * FROM space_marine " +
                    "WHERE id = " + sm.getId().toString());
            smSet.next();

            ResultSet chapterSet = getStm().executeQuery("SELECT * FROM chapter " +
                    "WHERE chapter_id = " + smSet.getInt("chapter_id"));
            chapterSet.next();

            ResultSet coordinatesSet = getStm().executeQuery("SELECT * FROM coordinates " +
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
    public int add(SpaceMarine sm) {
        try {
            return insertSpaceMarine(sm);
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return 0;
    }

    @Override
    public void remove(SpaceMarine sm) {

    }

    @Override
    public void register(User user) {
        username = user.getUsername();
    }

    // --- private method ---
    private <E extends Enum<E>> E getEnum(ResultSet rs, String name, Class<E> enumClass) throws SQLException {
        String SQL = "SELECT * FROM " + name +
                " WHERE " + name + "_id = " + rs.getInt(name + "_id");
        ResultSet resultSet = getStm().executeQuery(SQL);
        resultSet.next();
        if (resultSet.getString(name).equals("NULL")) return null;
        return Enum.valueOf(enumClass, resultSet.getString(name));
    }
    SpaceMarine getSpaceMarine(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        Coordinates coordinates = null;
        {
            String SQL = "SELECT * FROM coordinates " +
                    "WHERE coordinates_id = " + ((Integer)rs.getInt("coordinates_id")).toString();

            ResultSet resultSet = getStm().executeQuery(SQL);
            while (resultSet.next()) {
                coordinates = new Coordinates(resultSet.getInt("x"), resultSet.getInt("y"));
            }
        }
        Chapter chapter = null;
        {
            String SQL = "SELECT * FROM chapter " +
                    "WHERE chapter_id = " + ((Integer)rs.getInt("chapter_id")).toString();
            ResultSet resultSet = getStm().executeQuery(SQL);
            while (resultSet.next()) {
                chapter = new Chapter(resultSet.getString("name"), (long)resultSet.getInt("marines_count"));
            }
        }

        ZonedDateTime creationDate = rs.getDate("creation_date").toLocalDate().atStartOfDay(ZoneId.of("UTC+7"));
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
        chapterSet.insertRow();
    }
    private void updateCoordinates(ResultSet coordinatesSet, Coordinates coordinates) throws SQLException {
        coordinatesSet.updateInt("x", coordinates.getX());
        coordinatesSet.updateInt("y", coordinates.getY());
        coordinatesSet.insertRow();
    }
    private void updateSpaceMarine(ResultSet smSet, SpaceMarine sm, ResultSet chSet, ResultSet coorSet) throws SQLException {
        java.util.Date utilDate = (java.util.Date) java.util.Date.from(sm.getCreationDate().toInstant());
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        smSet.updateString("name", sm.getName());
        smSet.updateInt("coordinates_id", coorSet.getInt("coordinates_id"));
        smSet.updateInt("health", sm.getHealth());
        smSet.updateDate("creation_date", sqlDate);
        smSet.updateInt("astartes_category_id", sm.getCategory() == null ? 1 : sm.getCategory().ordinal());
        smSet.updateInt("weapon_id", sm.getWeaponType() == null ? 1 : sm.getWeaponType().ordinal());
        smSet.updateInt("melee_weapon_id", sm.getMeleeWeapon() == null ? 1 : sm.getMeleeWeapon().ordinal());
        smSet.updateInt("chapter_id", chSet.getInt("chapter_id"));
        smSet.updateString("created_by", username);
        smSet.insertRow();
    }
    private int insertSpaceMarine(SpaceMarine sm) throws SQLException {
        Statement chapterStm = getStm();
        ResultSet chapterSet = chapterStm.executeQuery("SELECT *  FROM chapter");
        chapterSet.moveToInsertRow();
        updateChapter(chapterSet, sm.getChapter());
        chapterStm.close();


        Statement coordinatesStm = getStm();
        ResultSet coordinatesSet = coordinatesStm.executeQuery("SELECT * FROM coordinates");
        coordinatesSet.moveToInsertRow();
        updateCoordinates(coordinatesSet, sm.getCoordinates());
        coordinatesStm.close();

        ResultSet coordinates_id = getStm().executeQuery("SELECT * FROM coordinates");
        coordinates_id.last();
        System.out.println(coordinates_id.getInt(1));

        ResultSet chapter_id = getStm().executeQuery("SELECT * FROM chapter");
        chapter_id.last();
        System.out.println(chapter_id.getInt(1));

        Statement smStm = getStm();
        ResultSet smSet = smStm.executeQuery("SELECT * FROM space_marine");
        smSet.moveToInsertRow();
        updateSpaceMarine(smSet, sm, chapter_id, coordinates_id);

        System.out.println(smSet.getInt("id"));
        return smSet.getInt("id");
    }
}
