package com.operatordb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;


public class Database {
    static Dotenv dotenv = Dotenv.load();
    
    static String db_url = dotenv.get("DB_URL");
    static String user = dotenv.get("DB_USER"); 
    static String password = dotenv.get("DB_PASSWORD");
    static Connection conn;

    public static void insertUser(String phone, String name, String surname, int balance, String operator, int status){
        try {
            conn = DriverManager.getConnection(db_url, user, password);
            String sql = "INSERT INTO users (phone, name, surname, balance, operator, status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, phone);
            ps.setString(2, name);
            ps.setString(3, surname);
            ps.setInt(4, balance);
            ps.setString(5, operator);
            ps.setInt(6, status);

            System.out.println("Inserted new user!");
            ps.executeUpdate();
            conn.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println("Failed to insert user!");
            e.printStackTrace();
        }
    }

    public static boolean doesUserExists(String phone){
        try {
            conn = DriverManager.getConnection(db_url, user, password);
            String sql = "SELECT COUNT(*) FROM users WHERE phone = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                if(rs.getInt(1) == 1){
                    System.out.println("User exists!");
                    return true;
                }
            }
            conn.close();
            ps.close();
            System.out.println("User does not exist!");
            return false;
        } catch (SQLException e) {
            System.out.println("Failed to check user!");
            e.printStackTrace();
            return false;
        }
    }


    
}
