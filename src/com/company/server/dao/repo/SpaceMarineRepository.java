package com.company.server.dao.repo;

import com.company.server.factory.CommandFactory;
import com.company.shared.entity.SpaceMarine;

public interface SpaceMarineRepository extends Repository {
    void loadDatabase(CommandFactory commandFactory );
    void update(SpaceMarine sm);
    void add(SpaceMarine sm);
    void remove(SpaceMarine sm);
}
