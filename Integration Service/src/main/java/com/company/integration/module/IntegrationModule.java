package com.company.integration.module;

import com.company.integration.util.DB;
import com.company.integration.util.DbException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegrationModule extends Thread{

    public void run(){

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        connection = DB.getConnection();

        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Integration module is running ...");

        //while(true) {

            try {
                StringBuilder sql = new StringBuilder().append("UPDATE tb_item SET integration_status = ? WHERE integration_status = 'SENT'");

                preparedStatement = connection.prepareStatement(sql.toString());

                preparedStatement.setString(1, "SYNCED");

                int rowsAffected = preparedStatement.executeUpdate();

                connection.commit();

                if (rowsAffected > 0) {
                    ResultSet rsTwo = preparedStatement.getGeneratedKeys();
                    while (rsTwo.next()) {
                        int id = rsTwo.getInt(1);
                        System.out.println("Done! Id: " + id);
                    }
                } else {
                    System.out.println("No rows affected!");
                }
            } catch (SQLException e) {
                try {
                    System.out.println("Rollback");
                    connection.rollback();
                    throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
                }
                catch (SQLException e1) {
                    throw new DbException("Error trying to rollback! Caused by: " + e1.getMessage());
                }
            }
            /*finally {
                DB.closeStatement(preparedStatement);
                DB.closeConnection();
            }*/

            // TODO

            System.out.println("Current thread ID: " + Thread.currentThread().getId());

            System.out.println("Thread looping ...");

            //Thread.currentThread().stop();
        }
    //}
    
}
