package com.company.server.dao.repo.impl;

import com.company.server.dao.repo.CoordinatesRepository;
import com.company.shared.entity.Coordinates;

import java.sql.*;

public class CoordinatesRepositoryImpl implements CoordinatesRepository {
    private Connection con;

    public CoordinatesRepositoryImpl(Connection con) {
        this.con = con;
    }

    @Override
    public int add(Coordinates coordinates) throws SQLException {
        String SQL = "INSERT INTO coordinates(x, y) VALUES(?, ?) RETURNING coordinates_id";

        PreparedStatement pst = con.prepareStatement(SQL);

        pst.setInt(1, coordinates.getX());

        pst.setInt(2, coordinates.getY());

        ResultSet rs = pst.executeQuery();

        rs.next();

        return rs.getInt("coordinates_id");
    }

    @Override
    public int update(Coordinates coordinates, int coordinates_id) throws SQLException {
        String SQL = "UPDATE coordinates SET " +
                "x = ?, " +
                "y = ?" +
                "WHERE coordinates_id = ? RETURNING coordinates_id";

        PreparedStatement pst = con.prepareStatement(SQL);

        pst.setInt(1, coordinates.getX());

        pst.setInt(2, coordinates.getY());

        pst.setInt(3, coordinates_id);

        ResultSet rs = pst.executeQuery();

        rs.next();

        return rs.getInt("coordinates_id");
    }

    @Override
    public Coordinates findById(int id) throws SQLException {
        String SQL = String.format("SELECT * FROM coordinates WHERE coordinates_id = %d", id);

        ResultSet rs = con.createStatement().executeQuery(SQL);

        rs.next();

        return new Coordinates(rs.getInt("x"), rs.getInt("y"));
    }
}
