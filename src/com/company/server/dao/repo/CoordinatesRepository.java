package com.company.server.dao.repo;

import com.company.shared.entity.Coordinates;

import java.sql.SQLException;

public interface CoordinatesRepository extends Repository {
    int add(Coordinates coordinates) throws SQLException;
    int update(Coordinates coordinates, int coordinates_id) throws SQLException;
    Coordinates findById(int id) throws SQLException;
}
