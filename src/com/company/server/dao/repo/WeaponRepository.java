package com.company.server.dao.repo;

import com.company.shared.entity.Weapon;

import java.sql.SQLException;

public interface WeaponRepository extends Repository {
    Weapon findById(int id) throws SQLException;
}
