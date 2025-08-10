/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author SABARIRAJAN
 */
public class empDB 
{
    public Connection connect() throws ClassNotFoundException, SQLException
    {
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bikers", "postgres", "1998");
        return conn;
    }
    
    public ArrayList<EmpDetails> retriveEmps() throws ClassNotFoundException, SQLException 
    {
        String sql = "SELECT * FROM employees";
        ArrayList<EmpDetails> list = new ArrayList<>();

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String gender = rs.getString(3);
                String phone = rs.getString(4);               // read phone as String
                String role = rs.getString(5);

                java.sql.Date sqlDate = rs.getDate(6);
                LocalDate joinDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                String username = rs.getString(7);
                String password = rs.getString(8);

                list.add(new EmpDetails(id, name, gender, phone, role, joinDate, username, password));
            }
        }

        return list;
    }
    
    public boolean deleteUserbyId(int empid) throws ClassNotFoundException, SQLException
    {
        Connection con = connect();
        String sql = "DELETE FROM employees WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, empid);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }
    
}

