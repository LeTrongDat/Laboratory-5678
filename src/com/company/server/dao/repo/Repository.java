package com.company.server.dao.repo;

import com.company.server.io.impl.Log;
import com.company.shared.entity.User;

import java.sql.SQLException;

public interface Repository {
    void register(User user);
    default void handleSQLException(SQLException e) {
        Log.logback("Message: " + e.getMessage());
        Log.logback("SQL State: " + e.getSQLState());
    }
}
