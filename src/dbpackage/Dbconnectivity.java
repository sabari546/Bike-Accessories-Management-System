package dbpackage;

import Employee.Model;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Dbconnectivity 
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
    
    public void insertUser(Model data) throws ClassNotFoundException, SQLException
    {
      Connection con=   connect();
      PreparedStatement ps=  con.prepareStatement("insert into product(name,category,price,stock,added_date) values(?,?,?,?,?)");
      ps.setString(1, data.getName());
      ps.setString(2, data.getCategory());
      ps.setInt(3, data.getPrice());
      ps.setInt(4, data.getStock());
      ps.setDate(5, (Date) data.getAdded_date());
      int res = ps.executeUpdate();
      System.out.println(res+ " row updated!");
   }
    
    public ArrayList<Model> retrieveProducts() throws ClassNotFoundException, SQLException
    {
        Connection con = connect();
        PreparedStatement psm=con.prepareStatement("select id, name, category, price, stock, added_date from product");
        ResultSet resultSet=psm.executeQuery();
        ArrayList<Model> list = new ArrayList();
        while(resultSet.next())
        {
             Model mo = new Model();
             mo.setId(resultSet.getInt(1));
             mo.setName(resultSet.getString(2));
             mo.setCategory(resultSet.getString(3));
             mo.setPrice(resultSet.getInt(4));
             mo.setStock(resultSet.getInt(5));
             mo.setAdded_date(resultSet.getDate(6));
             
             list.add(mo);
                  
             
         }
         return list;
    }
    
    public boolean insertSale(int orderid ,String productName, int quantity, double amount) throws SQLException, ClassNotFoundException 
    {
        Connection con = connect();
        String sql = "INSERT INTO sales(orderid, productname, quantity, amount, solddate) VALUES (?, ?, ?, ?, CURRENT_DATE)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, orderid);
        ps.setString(2, productName);
        ps.setInt(3, quantity);
        ps.setDouble(4, amount);
        
        return ps.executeUpdate() > 0;
    }

    public boolean updateProductQuantity(int productId, int soldQty) throws SQLException, ClassNotFoundException
    {
        Connection con = connect();
        String sql = "UPDATE product SET stock = stock - ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, soldQty);
        ps.setInt(2, productId);
        return ps.executeUpdate() > 0;
    }
    
    public boolean deleteProductById(int productId) throws ClassNotFoundException, SQLException 
    {
        Connection con = connect();
        String sql = "DELETE FROM product WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, productId);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }

    public ArrayList<Model> lowStockList() throws ClassNotFoundException, SQLException
    {
        Connection con = connect();
        String sql = "select id, name, category, price, stock from product where stock <= 5 ";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        ArrayList<Model> list = new ArrayList<>();
        
        while(rs.next())
        {
            Model mo = new Model();
            mo.setId(rs.getInt(1));
            mo.setName(rs.getString(2));
            mo.setCategory(rs.getString(3));
            mo.setPrice(rs.getInt(4));
            mo.setStock(rs.getInt(5));
            
            list.add(mo);
        }
        
        return list;
    }
    
}

    

