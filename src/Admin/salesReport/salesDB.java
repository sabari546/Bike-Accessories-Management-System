/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.salesReport;

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
public class salesDB 
{
    public Connection connect() throws ClassNotFoundException, SQLException
    {
        Class.forName("org.postgresql.Driver");
        String uname = "postgres";
        String pass = "1998";
        String url = "jdbc:postgresql://localhost:5432/bikers";
        Connection con = DriverManager.getConnection(url, uname, pass);
        return con;
    }
    
    public ArrayList<SalesReport> getdatas() throws ClassNotFoundException, SQLException
    {
        Connection con = connect();
        PreparedStatement ps =con.prepareStatement("SELECT orderid, productname, quantity, amount, soldDate FROM sales");
        ResultSet rs = ps.executeQuery();
        
        ArrayList<SalesReport> list = new ArrayList();
        while(rs.next())
        {
            SalesReport sr = new SalesReport(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getInt(3),
                    rs.getInt(4),
                    rs.getDate(5).toLocalDate()
            );
            list.add(sr);
            
        }
        return list;
    }
        
    
}
