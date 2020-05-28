package com.company.server.dao.repo.impl;

import com.company.shared.entity.User;
import com.company.server.dao.repo.SpaceMarineRepository;
import com.company.shared.entity.SpaceMarine;

import java.sql.Connection;

public class SpaceMaineRepositoryImpl implements SpaceMarineRepository {

    private User user;
    private Connection connection;

    public SpaceMaineRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void loadDatabase() {
        String SQL = String.format("SELECT * FROM space_marine WHERE created_by = %s", user.getName());

    }

    @Override
    public void update(SpaceMarine sm) {
    }

    @Override
    public void add(SpaceMarine sm) {
    }

    @Override
    public void remove(SpaceMarine sm) {

    }
}
