package Admin;

import dbpackage.Dbconnectivity;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



public class AdminAreaController implements Initializable {
    
    @FXML
    private Button logoutButton;
    @FXML
    private Label totalProd;
    @FXML
    private Label LowStockStatus;
    @FXML
    private Label todaySales;
    @FXML
    private Button addProduct;
    @FXML
    private Button viewInventory;
    @FXML
    private Button viewsales;
    @FXML
    private Button manageEmp;
    private Object stage;
    @FXML
    private AnchorPane ADMINsCENE;
    @FXML
    private AnchorPane mainpane;
  
    
    Dbconnectivity db = new Dbconnectivity();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        try 
        {
            int totalCount = getTotalProductCount();
            totalProd.setText(String.valueOf(totalCount));


            todaySales.setText(String.valueOf(getTodaySalesCount()));

        } 
      catch (ClassNotFoundException | SQLException ex) 
        {
            Logger.getLogger(AdminAreaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try
        {
            List<String> categories = getLowStockCategories();
            if (categories.isEmpty()) 
            {
                LowStockStatus.setText("All categories are sufficiently stocked");
            } 
            else 
            {
                String categoryList = String.join(", ", categories);
                LowStockStatus.setText("Low stock in: " + categoryList);
            }
    } catch (Exception e) {
        e.printStackTrace();
    }
        
        
    }    

    @FXML
    private void logoutButton(ActionEvent event) {
        try 
        {
            // Load login scene
            Stage st = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/javafxapplication13/loginScene.fxml"));

            Scene sc = new Scene(root);
            st.setScene(sc);
            st.show();
            
            ADMINsCENE.getScene().getWindow().hide();
        
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void addproductButton(ActionEvent event) throws IOException 
    {
        
        // Load FXML as AnchorPane or any layout
        AnchorPane root = FXMLLoader.load(getClass().getResource("Addproducts.fxml"));

        // Set it inside the center of your BorderPane
        mainpane.getChildren().setAll(root);

        
        
    }

    @FXML
    private void onViewInventoryClick(ActionEvent event) throws IOException {
        
        AnchorPane root = FXMLLoader.load(getClass().getResource("viewAdminInventory.fxml"));

        
        mainpane.getChildren().setAll(root);
        
    }
    
    public int getTotalProductCount() throws ClassNotFoundException, SQLException 
    {
        int count = 0;
        Connection con = db.connect();
        PreparedStatement psm=con.prepareStatement("select sum(stock) from product");
        ResultSet rs =  psm.executeQuery();
        
        while(rs.next())
        {
            count = rs.getInt(1);
        }
        
        return count;
        
    }
    
    private List<String> getLowStockCategories() throws ClassNotFoundException, SQLException 
    {
        List<String> lowStockCategories = new ArrayList<>();
            Connection con = db.connect();
            String query = "SELECT DISTINCT category FROM product WHERE stock < 5";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            if (rs.next())
            {
                lowStockCategories.add(rs.getString(1));
            }
        return lowStockCategories;
    }
    
    private int getTodaySalesCount() throws ClassNotFoundException, SQLException 
    {
        int count = 0;
        Connection con = db.connect();
        String query = "SELECT COUNT(*) FROM sales WHERE solddate = CURRENT_DATE";
        PreparedStatement pst = con.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) 
        {
            count = rs.getInt(1);
        }

        return count;
    }

    @FXML
    private void handleSalesReport(ActionEvent event) throws IOException 
    {
        AnchorPane root = FXMLLoader.load(getClass().getResource("salesReport/SalesReport.fxml"));
        mainpane.getChildren().setAll(root);
    }

    @FXML
    private void handleManageEmployee(ActionEvent event) throws IOException 
    {
       Parent root = FXMLLoader.load(getClass().getResource("/Admin/manageEmp.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Manage Employees");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void lowStockBtn(ActionEvent event) throws IOException 
    {
        Parent root = FXMLLoader.load(getClass().getResource("lowStockdisplay.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Manage low Stock");
        stage.show();
        
        
    }


    
    
    
}
