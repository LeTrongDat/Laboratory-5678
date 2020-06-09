package com.company.server.dao.repo.impl;

import com.company.server.dao.repo.AstartesCategoryRepository;
import com.company.shared.entity.AstartesCategory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AstartesCategoryRepositoryImpl implements AstartesCategoryRepository {
    private Connection con;

    public AstartesCategoryRepositoryImpl(Connection con) {
        this.con = con;
    }

    @Override
    public AstartesCategory findById(int id) throws SQLException {
        String SQL = String.format("SELECT * FROM astartes_category WHERE astartes_category_id = %d", id);

        ResultSet rs = con.createStatement().executeQuery(SQL);

        rs.next();

        return rs.getString("astartes_category").equals("NULL")
                ? null
                : AstartesCategory.valueOf(rs.getString("astartes_category"));
    }
}
