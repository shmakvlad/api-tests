package com.affise.api.database;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

import static com.affise.api.config.Config.getConfig;

@Slf4j
public class ConnectToMySql {

    private static final String url = getConfig().mysqlUrl();
    private static final String user = getConfig().mysqlUser();
    private static final String password = getConfig().mysqlPassword();

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;


    public ConnectToMySql() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            try {
                connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                statement.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    @SneakyThrows
    public void deleteAffiliateFromMySql(int partnerId) {
        String query = "DELETE FROM partner WHERE id = " + partnerId;
        statement.executeUpdate(query);
        log.info("Affiliates {} successfully delete from MySql", partnerId);
    }

    public void closeConnection() {
        try {
            if (connection != null){
                connection.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if(statement != null){
                statement.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (resultSet != null){
                resultSet.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }


}
