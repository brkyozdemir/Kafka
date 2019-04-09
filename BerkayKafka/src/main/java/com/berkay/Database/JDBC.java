package com.berkay.Database;


import java.sql.*;

public class JDBC {
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost/";
    private final String DB_NAME = "kafka";
    private final String USER = "root";
    private final String PASS = "";

    private PreparedStatement preparedStatement;
    private Connection conn;
    private Statement stmt;
    private int execution;

    public void createTable() {
        conn = null;
        stmt = null;
        try{
            getConnection();
            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS DASHBOARD " +
                    "(wholeMessage VARCHAR(255), " +
                    " city VARCHAR(255)," +
                    " currentDate VARCHAR(255), " +
                    " currentHour VARCHAR(255))";

            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            handleConn();
        }
        System.out.println("Goodbye!");
    }

    public void insertTable(String message, String city, String currentDate, String currentHour){
        conn = null;
        stmt = null;
        try{
            getConnection();
            System.out.println("Inserting records into the table...");
            preparedStatement = conn.prepareStatement("INSERT INTO dashboard (wholeMessage, city, currentDate, currentHour) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1,message);
            preparedStatement.setString(2,city);
            preparedStatement.setString(3,currentDate);
            preparedStatement.setString(4,currentHour);
            preparedStatement.executeUpdate();
            System.out.println("Inserted records into the table...");

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            handleConn();
        }
    }

    public void createDatabase() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            execution = stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS kafka");
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleConn(){
        try{
            if(stmt != null)
                conn.close();
        }catch(SQLException se){

        }
        try{
            if(conn != null)
                conn.close();
        }catch(SQLException se){
            se.printStackTrace();
        }
    }

    public void getConnection() throws Exception {
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL+DB_NAME, USER, PASS);
        System.out.println("Connected database successfully...");
    }
}