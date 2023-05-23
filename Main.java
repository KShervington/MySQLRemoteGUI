package com.company;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static MysqlDataSource dataSource = null;

    public static void makeConnection() {
        dataSource = new MysqlDataSource();
        dataSource.setURL(DisplayQueryResults.dbUrl);
        dataSource.setUser(DisplayQueryResults.dbUserName);
        dataSource.setPassword(DisplayQueryResults.dbPassword);
    }

    public static void main(String[] args) throws SQLException {

        DisplayQueryResults window = new DisplayQueryResults();
    }
}
