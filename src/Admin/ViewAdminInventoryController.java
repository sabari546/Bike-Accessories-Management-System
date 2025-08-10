/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import dbpackage.Dbconnectivity;
import Employee.Model;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


public class ViewAdminInventoryController implements Initializable {
    @FXML
    private TableView<Model> productTable;
    @FXML
    private TableColumn<Model, Integer> colid;
    @FXML
    private TableColumn<Model, String> colName;
    @FXML
    private TableColumn<Model, String> colCategory;
    @FXML
    private TableColumn<Model, Integer> colPrice;
    @FXML
    private TableColumn<Model, Integer> colStock;
    @FXML
    private TableColumn<Model, String> colAddeddate;
    @FXML
    private Button deletedata;
    @FXML
    private Button editbutton;
    @FXML
    private ComboBox<String> categoryComboBox;

    private ObservableList<Model> allProducts = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        loadProducts(); // Load all products from DB
        loadCategories();   
        
    }    
    
    private void loadProducts()
    {
    colAddeddate.setCellValueFactory(cellData -> {
        Date date = cellData.getValue().getAdded_date();
        String formattedDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
        return new javafx.beans.property.SimpleStringProperty(formattedDate);
    });

        try 
        {
            Dbconnectivity dao = new Dbconnectivity();
            ArrayList<Model> datas = dao.retrieveProducts();

            allProducts.clear();
            allProducts.addAll(datas);

            productTable.setItems(allProducts);
            loadCategories(); // Load comboBox values here

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ViewAdminInventoryController.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    
    private void loadCategories() 
    {
    java.util.Set<String> categories = new java.util.HashSet<>();
    for (Model p : allProducts) 
    {
        categories.add(p.getCategory());
    }
    categoryComboBox.getItems().clear();
    categoryComboBox.getItems().add("All Categories"); // Optional default
    categoryComboBox.getItems().addAll(categories);
    categoryComboBox.setValue("All Categories");

    categoryComboBox.setOnAction(e -> filterByCategory());
    }

    private void filterByCategory()
    {
    String selectedCategory = categoryComboBox.getValue();

    if (selectedCategory.equals("All Categories"))
    {
        productTable.setItems(allProducts);
    } 
    else 
    {
        javafx.collections.ObservableList<Model> filteredList = javafx.collections.FXCollections.observableArrayList();
        for (Model p : allProducts) 
        {
            if (p.getCategory().equalsIgnoreCase(selectedCategory))
            {
                filteredList.add(p);
            }
        }
            productTable.setItems(filteredList);
    }
    }


    @FXML
    private void OnDeletebtn(ActionEvent event) 
    {
         Model selectedProduct = productTable.getSelectionModel().getSelectedItem();
          
        if (selectedProduct == null)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product to delete.");
            alert.showAndWait();
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete this product?");
        Optional<ButtonType> result = confirm.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK)
        {
        try {
            Dbconnectivity dao = new Dbconnectivity();
            boolean deleted = dao.deleteProductById(selectedProduct.getId());

            if (deleted) {
                allProducts.remove(selectedProduct);
                productTable.refresh();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Product deleted successfully.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete product from database.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
         
    

    @FXML
    private void OnEditbtn(ActionEvent event) throws IOException {
        Stage st = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("editproduct/Editproduct.fxml"));
        
        Scene scene = new Scene(root);
        
        st.setScene(scene);
        st.show();
    }
    
}
