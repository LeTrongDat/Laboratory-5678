package com.company.server.dao.repo;

import com.company.server.io.impl.Log;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public interface Repository {

    default void handleSQLException(@NotNull SQLException e) {
        Log.logback("Message: " + e.getMessage());
        Log.logback("SQL State: " + e.getSQLState());
        Log.logback("Localize: " + e.getLocalizedMessage());
        Log.logback("Error Code: " + e.getErrorCode());
        e.printStackTrace();
    }
}
