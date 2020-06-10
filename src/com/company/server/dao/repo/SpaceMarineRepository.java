package com.company.server.dao.repo;

import com.company.server.factory.CommandFactory;
import com.company.shared.entity.SpaceMarine;
import com.company.shared.entity.User;

import java.sql.SQLException;

public interface SpaceMarineRepository extends Repository {
    void loadDatabase(CommandFactory commandFactory) throws SQLException;
    int update(SpaceMarine sm) throws SQLException;
    int add(SpaceMarine sm) throws SQLException;
    void register(User user);
    void removeById(int id) throws SQLException;
}
