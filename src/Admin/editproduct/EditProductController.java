/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin.editproduct;

import dbpackage.Dbconnectivity;
import Employee.Model;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author SABARIRAJAN
 */
public class EditProductController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField stockfield;
    @FXML
    private DatePicker datePickerField;

    private int id;

    private Model product;

    @FXML
    private Button UpdateBtn;
    @FXML
    private TextField idField;
    @FXML
    private Label updateLabel;
    @FXML
    private AnchorPane updateScene;

    public void setProductData(Model product) {
        this.product = product;
        this.id = product.getId();

        nameField.setText(product.getName());
        categoryField.setText(product.getCategory());
        priceField.setText(String.valueOf(product.getPrice()));
        stockfield.setText(String.valueOf(product.getStock()));

        Instant instant = product.getAdded_date().toInstant();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        datePickerField.setValue(localDate);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleUpdateButton(ActionEvent event) 
    {
        String idText = idField.getText().trim();
        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        String priceText = priceField.getText().trim();
        String stockText = stockfield.getText().trim();
        LocalDate addedDate = datePickerField.getValue();

        // Check if any field is empty
        if (idText.isEmpty() || name.isEmpty() || category.isEmpty() ||
            priceText.isEmpty() || stockText.isEmpty() || addedDate == null) {
            updateLabel.setText("Please fill all fields ⚠️");
            return;
        }

        //  Try parsing numbers safely
        int id, price, stock;
        try {
            id = Integer.parseInt(idText);
            price = Integer.parseInt(priceText);
            stock = Integer.parseInt(stockText);
        } catch (NumberFormatException e) {
            updateLabel.setText("ID, Price, and Stock must be valid numbers ⚠️");
            return;
        }

        // Extra numeric validation
        if (price <= 0 || stock < 0) {
            updateLabel.setText("Price must be > 0 and Stock ≥ 0 ⚠️");
            return;
        }

        // Proceed with DB update
        try {
            updateProductInDatabase(id, name, category, price, stock, addedDate);
        }
        catch (Exception ex) 
        {
            ex.printStackTrace();
            updateLabel.setText("Error updating product ⚠️");
        }
    }


    private void updateProductInDatabase(int id, String name, String category, int price, int stock, LocalDate addedDate) throws ClassNotFoundException, SQLException {
        Dbconnectivity db = new Dbconnectivity();
        Connection con = db.connect();
        PreparedStatement ps = con.prepareStatement("update product set name =?, category = ?, price = ?, stock = ?, added_date = ? where id = ?");
        ps.setString(1, name);
        ps.setString(2, category);
        ps.setInt(3, price);
        ps.setInt(4, stock);
        ps.setDate(5, Date.valueOf(addedDate));
        ps.setInt(6, id);

        int rowsUpdated = ps.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Product updated successfully!");
            updateLabel.setText("updated succesfully!!👍");
            Alert al =new Alert(Alert.AlertType.CONFIRMATION);
            al.setContentText("product updated successfully!!");
            al.showAndWait();
            updateScene.getScene().getWindow().hide();
        } else {
            System.out.println("No product found with ID: " + id);
            updateLabel.setText("No product found with your given ID ⚠️");

        }
    }

}
