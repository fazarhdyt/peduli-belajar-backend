package com.binar.pedulibelajar.model;

import java.sql.*;

public class UpdatePassword {
    public static void updatePassword(String username, String newPassword) {
        String url = "jdbc:postgresql://localhost/test";
        String user = "username";
        String password = "password";

        String sql = "UPDATE users SET password = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding parameters
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);

            // update
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        updatePassword("test_user", "new_password");
    }
}
