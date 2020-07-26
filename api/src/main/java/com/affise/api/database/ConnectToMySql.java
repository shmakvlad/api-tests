package com.affise.api.database;


import io.qameta.allure.Step;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
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
    public void createAffiliate(String...query) {
        for (String q : query){
            statement.executeUpdate(q);
            log.info("Affiliate successfully create in MySql");
        }
    }

    @SneakyThrows @Step
    public void deleteAffiliateFromMySql(int partnerId) {
        String query = "DELETE FROM partner WHERE id = " + partnerId;
        statement.executeUpdate(query);
        log.info("Affiliate {} successfully delete from MySql", partnerId);
    }

    @SneakyThrows
    public void deleteAffiliates(Integer...partnerId) {
        for (Integer i : partnerId){
            String query = "DELETE FROM partner WHERE id = " + i;
            statement.executeUpdate(query);
            log.info("Affiliate {} successfully delete from MySql", i);
        }
    }

    @SneakyThrows @Step
    public void existsInMySql(int partnerId) {
        String query = "SELECT EXISTS(SELECT id FROM partner WHERE id = " + partnerId + ")";
        resultSet = statement.executeQuery(query);
        resultSet.first();
        if (resultSet.getBoolean(1)) {
            if (getConfig().logging()) {
                log.info("Affiliate {} exists in MySql", partnerId);
            }
        }
        else {
            if (getConfig().logging()) {
                log.info("Affiliate {} not exists in MySql", partnerId);
            }
            throw new IOException("affiliate not saved in MySql");
        }
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
