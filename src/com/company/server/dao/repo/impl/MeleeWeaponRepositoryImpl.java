package com.company.server.dao.repo.impl;

import com.company.server.dao.repo.MeleeWeaponRepository;
import com.company.shared.entity.MeleeWeapon;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MeleeWeaponRepositoryImpl implements MeleeWeaponRepository {
    private Connection con;

    public MeleeWeaponRepositoryImpl(Connection con) {
        this.con = con;
    }

    @Override
    public MeleeWeapon findById(int id) throws SQLException {
        String SQL = String.format("SELECT * FROM melee_weapon WHERE melee_weapon_id = %d", id);

        ResultSet rs = con.createStatement().executeQuery(SQL);

        rs.next();

        return rs.getString("melee_weapon").equals("NULL")
                ? null
                : MeleeWeapon.valueOf(rs.getString("melee_weapon"));
    }
}
