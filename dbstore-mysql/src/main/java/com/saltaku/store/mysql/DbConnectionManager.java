package com.saltaku.store.mysql;

import java.sql.Connection;
import java.sql.SQLException;

public interface DbConnectionManager {
Connection getConnection() throws SQLException;
void close();
}
