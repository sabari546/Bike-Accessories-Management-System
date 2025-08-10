package Admin.salesReport;


import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class SalesReportController implements Initializable {
    @FXML
    private TableView<SalesReport> salesTable;
    @FXML
    private TableColumn<SalesReport, Integer> orderidCol;
    @FXML
    private TableColumn<SalesReport, String> productCol;
    @FXML
    private TableColumn<SalesReport, Integer> quantityCol;
    @FXML
    private TableColumn<SalesReport, Integer> amountCol;
    @FXML
    private TableColumn<SalesReport, String> dateCol;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private Label totalsalesLabel;
    private ObservableList<SalesReport> salesList = FXCollections.observableArrayList();
    
    @FXML
    private Button FilterButton;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try 
        {
             loadSalesdata();  // ✅ load data and fill salesList
        }
        catch (Exception ex) {
        Logger.getLogger(SalesReportController.class.getName()).log(Level.SEVERE, null, ex);
    }
        
        
    }    
    
   private void loadSalesdata() throws ClassNotFoundException, SQLException 
   {
        //salesList.clear();
       
        Class.forName("org.postgresql.Driver");
        String uname = "postgres";
        String pass = "1998";
        String url = "jdbc:postgresql://localhost:5432/bikers";
        Connection con = DriverManager.getConnection(url, uname, pass);

        String sql = "SELECT orderid, productname, quantity, amount, solddate FROM sales";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        double total = 0;
        while (rs.next()) {
            SalesReport data = new SalesReport(
                rs.getInt("orderid"),
                rs.getString("productname"),
                rs.getInt("quantity"),
                rs.getDouble("amount"),
                rs.getDate("solddate").toLocalDate()
            );
//            salesList.add(data);
            salesTable.getItems().add(data);
            total += data.getTotalAmount();
        }

        //salesTable.setItems(salesList);  // ✅ now filtering will work
        totalsalesLabel.setText(String.format("%.2f", total));
        con.close();
    }
   
    @FXML
    private void filterbydate(ActionEvent event) 
    {
    LocalDate from = fromDate.getValue();
    LocalDate to = toDate.getValue();

    if (from == null || to == null) return;

    salesTable.getItems().clear();  // clear current table

    try {
        Class.forName("org.postgresql.Driver");
        String uname = "postgres";
        String pass = "1998";
        String url = "jdbc:postgresql://localhost:5432/bikers";
        Connection con = DriverManager.getConnection(url, uname, pass);

        String sql = "SELECT orderid, productname, quantity, amount, solddate FROM sales WHERE solddate BETWEEN ? AND ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setDate(1, java.sql.Date.valueOf(from));
        ps.setDate(2, java.sql.Date.valueOf(to));
        ResultSet rs = ps.executeQuery();

        double total = 0;
        while (rs.next()) {
            SalesReport data = new SalesReport(
                rs.getInt("orderid"),
                rs.getString("productname"),
                rs.getInt("quantity"),
                rs.getDouble("amount"),
                rs.getDate("solddate").toLocalDate()
            );
            salesTable.getItems().add(data);
            total += data.getTotalAmount();
        }

        totalsalesLabel.setText(String.format("%.2f", total));
        con.close();
    } 
    catch (Exception ex)
    {
        Logger.getLogger(SalesReportController.class.getName()).log(Level.SEVERE, null, ex);
    }
}



//    @FXML
//    private void filterbydate(ActionEvent event) 
//    {
//        LocalDate from = fromDate.getValue();
//        LocalDate to = toDate.getValue();
//
//        if (from == null || to == null) return;
//
//        ObservableList<SalesReport> filtered = FXCollections.observableArrayList();
//        double total = 0;
//
//        for (SalesReport data : salesList) {
//            if ((data.getSaleDate().isEqual(from) || data.getSaleDate().isAfter(from)) &&
//                (data.getSaleDate().isEqual(to) || data.getSaleDate().isBefore(to))) {
//                filtered.add(data);
//                total += data.getTotalAmount();
//            }
//        }
//
//        salesTable.setItems(filtered);
//        totalsalesLabel.setText(String.format("%.2f", total));
//    }
}
