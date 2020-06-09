package com.company.server.dao.repo;

import com.company.shared.entity.MeleeWeapon;

import java.sql.SQLException;

public interface MeleeWeaponRepository extends Repository {
    MeleeWeapon findById(int id) throws SQLException;
}
