package com.company.server.dao.repo;

import com.company.shared.entity.AstartesCategory;

import java.sql.SQLException;

public interface AstartesCategoryRepository extends Repository {
    AstartesCategory findById(int id) throws SQLException;
}
