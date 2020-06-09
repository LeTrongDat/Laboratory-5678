package com.company.server.dao.repo.impl;

import com.company.server.dao.repo.WeaponRepository;
import com.company.shared.entity.Weapon;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WeaponRepositoryImpl implements WeaponRepository {
    private Connection con;

    public WeaponRepositoryImpl(Connection con) {
        this.con = con;
    }

    @Override
    public Weapon findById(int id) throws SQLException {
        String SQL = String.format("SELECT * FROM weapon WHERE weapon_id = %d", id);

        ResultSet rs = con.createStatement().executeQuery(SQL);

        rs.next();

        return rs.getString("weapon").equals("NULL")
                ? null
                : Weapon.valueOf(rs.getString("weapon"));
    }
}
