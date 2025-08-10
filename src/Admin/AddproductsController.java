package Admin;

import dbpackage.Dbconnectivity;
import Employee.Model;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AddproductsController implements Initializable 
{

    @FXML
    private TextField productname;
    @FXML
    private DatePicker addeddate;
    @FXML
    private TextField productPrice;
    @FXML
    private TextField stockqty;
    @FXML
    private Button submitButton;
    @FXML
    private AnchorPane addproductsScene;
    @FXML
    private ComboBox<String> categoryCombo;

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        categoryCombo.getItems().addAll(
            "Styling & Customization",
            "Luggage Solutions",
            "Lighting & Electrical",
            "Helmet",
            "Riding Gloves",
            "Electronics & Mounts",
            "Weather Protection",
            "Protection & Safety",
            "Cleaning & Maintenance",
            "Comfort & Touring"
        );
    }

    @FXML
    private void cancel(ActionEvent event) {
        addproductsScene.getScene().getWindow().hide();
    }

    @FXML
    private void submitButton(ActionEvent event) {
        String prodname = productname.getText();
        String category = categoryCombo.getValue();
        String priceText = productPrice.getText();
        String stockText = stockqty.getText();
        LocalDate localDate = addeddate.getValue();

        // Validate required fields
        if (prodname.isEmpty() || category == null || priceText.isEmpty() || stockText.isEmpty() || localDate == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill all fields correctly:\n- Product Name\n- Date\n- Category\n- Price (numeric)\n- Stock Quantity (numeric)");
            alert.showAndWait();
            return;
        }

        int price;
        int stock;

        // Validate numeric fields
        try {
            price = Integer.parseInt(priceText);
            stock = Integer.parseInt(stockText);
        } catch (NumberFormatException e) 
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Price and Stock Quantity must be valid numbers.");
            alert.showAndWait();
            return;
        }

        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);

        Model mo = new Model();
        mo.setName(prodname);
        mo.setCategory(category);
        mo.setPrice(price);
        mo.setStock(stock);
        mo.setAdded_date(sqlDate);

        Dbconnectivity db = new Dbconnectivity();
        try 
        {
            db.insertUser(mo);
            Alert al = new Alert(Alert.AlertType.CONFIRMATION);
            al.setContentText("Added successfully!!");
            al.showAndWait();
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            Logger.getLogger(AddproductsController.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Database error. Please try again.");
            alert.showAndWait();
        }
    }
}
