package com.company.server.dao.repo;

import com.company.server.factory.CommandFactory;
import com.company.shared.entity.SpaceMarine;

import java.util.List;

public interface SpaceMarineRepository extends Repository {
    void loadDatabase(CommandFactory commandFactory);
    void update(SpaceMarine sm);
    int add(SpaceMarine sm);
    void remove(SpaceMarine sm);
}
