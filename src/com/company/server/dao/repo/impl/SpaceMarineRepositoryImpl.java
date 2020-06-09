package com.company.server.dao.repo.impl;

import com.company.server.dao.repo.*;
import com.company.server.factory.CommandFactory;
import com.company.shared.entity.*;

import java.sql.*;
import java.time.ZoneId;


public class SpaceMarineRepositoryImpl implements SpaceMarineRepository {
    private Connection con;

    private ChapterRepository chapterRepo;

    private CoordinatesRepository coordinatesRepo;

    private AstartesCategoryRepository astartesCategoryRepo;

    private MeleeWeaponRepository meleeWeaponRepo;

    private WeaponRepository weaponRepo;

    private User user;

    public SpaceMarineRepositoryImpl(Connection con) {
        this.con = con;

        chapterRepo = new ChapterRepositoryImpl(con);

        coordinatesRepo = new CoordinatesRepositoryImpl(con);

        astartesCategoryRepo = new AstartesCategoryRepositoryImpl(con);

        meleeWeaponRepo = new MeleeWeaponRepositoryImpl(con);

        weaponRepo = new WeaponRepositoryImpl(con);
    }

    @Override
    public void loadDatabase(CommandFactory commandFactory) throws SQLException {
        String SQL = String.format("SELECT * FROM space_marine WHERE created_by = '%s'", user.getUsername());

        ResultSet rs = con.createStatement().executeQuery(SQL);

        while (rs.next()) {

            SpaceMarine sm = new SpaceMarine();

            sm.setId(rs.getInt("id"));

            sm.setName(rs.getString("name"));

            sm.setCoordinates(coordinatesRepo.findById(rs.getInt("coordinates_id")));

            sm.setCreationDate(rs.getDate("creation_date").toLocalDate().atStartOfDay(ZoneId.of("UTC+7")));

            sm.setHealth(rs.getInt("health"));

            sm.setCategory(astartesCategoryRepo.findById(rs.getInt("astartes_category_id")));

            sm.setMeleeWeapon(meleeWeaponRepo.findById(rs.getInt("melee_weapon_id")));

            sm.setWeaponType(weaponRepo.findById(rs.getInt("weapon_id")));

            sm.setChapter(chapterRepo.findById(rs.getInt("chapter_id")));

            commandFactory.add(sm);
        }
    }

    @Override
    public int update(SpaceMarine sm) throws SQLException {
        String SQL = "UPDATE space_marine SET " +
                "name = ?, " +
                "creation_date = ?, " +
                "health = ?, " +
                "astartes_category_id = ?, " +
                "weapon_id = ?, " +
                "melee_weapon_id = ? " +
                "WHERE id = ? AND created_by = ? RETURNING id, coordinates_id, chapter_id";

        PreparedStatement pst = con.prepareStatement(SQL);

        pst.setString(1, sm.getName());

        pst.setDate(2, Date.valueOf(sm.getCreationDate().toLocalDate()));

        pst.setInt(3, sm.getHealth());

        pst.setInt(4, sm.getCategory() == null ? 1 : sm.getCategory().ordinal() + 2);

        pst.setInt(5, sm.getWeaponType() == null ? 1 : sm.getWeaponType().ordinal() + 2);

        pst.setInt(6, sm.getMeleeWeapon() == null ? 1 : sm.getMeleeWeapon().ordinal() + 2);

        pst.setInt(7, sm.getId());

        pst.setString(8, user.getUsername());

        ResultSet rs = pst.executeQuery();

        if (!rs.next()) return -1;

        coordinatesRepo.update(sm.getCoordinates(), rs.getInt("coordinates_id"));

        chapterRepo.update(sm.getChapter(), rs.getInt("chapter_id"));

        return rs.getInt("id");
    }

    @Override
    public int add(SpaceMarine sm) throws SQLException {
        String SQL = "INSERT INTO space_marine(" +
                "name, " +
                "coordinates_id, " +
                "creation_date, " +
                "health, " +
                "astartes_category_id, " +
                "weapon_id, " +
                "melee_weapon_id, " +
                "chapter_id, " +
                "created_by" +
                ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        PreparedStatement pst = con.prepareStatement(SQL);

        pst.setString(1, sm.getName());

        pst.setInt(2, coordinatesRepo.add(sm.getCoordinates()));

        pst.setDate(3, Date.valueOf(sm.getCreationDate().toLocalDate()));

        pst.setInt(4, sm.getHealth());

        pst.setInt(5, sm.getCategory() == null ? 1 : sm.getCategory().ordinal() + 2);

        pst.setInt(6, sm.getWeaponType() == null ? 1 : sm.getWeaponType().ordinal() + 2);

        pst.setInt(7, sm.getMeleeWeapon() == null ? 1 : sm.getMeleeWeapon().ordinal() + 2);

        pst.setInt(8, chapterRepo.add(sm.getChapter()));

        pst.setString(9, user.getUsername());

        ResultSet rs = pst.executeQuery();

        rs.next();

        return rs.getInt("id");
    }


    @Override
    public void register(User user) {
        this.user = user;
    }
}
