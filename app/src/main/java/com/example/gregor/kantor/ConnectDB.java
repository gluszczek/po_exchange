package com.example.gregor.kantor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Gregor on 18.05.2017.
 */

public class ConnectDB {

    Statement statement;
    Connection connection;

    public ConnectDB(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://eu-cdbr-azure-west-d.cloudapp.net:3306/po","be4c47fc4d88f7","b7a9352d");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void showOffices(){
        try {
            statement = connection.createStatement();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        ResultSet rs = null;
        try {
            rs = statement.executeQuery("SELECT * FROM exchange;");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        try {
            while (rs.next()) {
                System.out.println(rs.getString(0));
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void ConnectDatabase(){
        try{

            //here sonoo is database name, root is username and password

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }catch(Exception e){ System.out.println(e);}
    }
}