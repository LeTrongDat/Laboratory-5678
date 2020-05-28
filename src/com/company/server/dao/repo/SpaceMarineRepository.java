package com.company.server.dao.repo;

import com.company.shared.entity.SpaceMarine;

public interface SpaceMarineRepository  {
    void loadDatabase();
    void update(SpaceMarine sm);
    void add(SpaceMarine sm);
    void remove(SpaceMarine sm);
}
