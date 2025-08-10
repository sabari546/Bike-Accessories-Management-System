package Employee;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class EmployeeDocumentController implements Initializable {

    // Product Table
    @FXML
    private TableView<Model> productTable;
    @FXML
    private TableColumn<Model, Integer> colProductID;
    @FXML
    private TableColumn<Model, String> colProductname;
    @FXML
    private TableColumn<Model, Integer> colPrice;
    @FXML
    private TableColumn<Model, Integer> colQuantity;

    // Cart Table
    @FXML
    private TableView<CartItem> cartTable;
    @FXML
    private TableColumn<CartItem, Integer> cartColProductID;
    @FXML
    private TableColumn<CartItem, String> cartColProductName;
    @FXML
    private TableColumn<CartItem, Double> cartColPrice;
    @FXML
    private TableColumn<CartItem, Integer> cartColQuantity;
    @FXML
    private TableColumn<CartItem, Double> cartColTotal;

    // Other UI controls
    @FXML
    private ComboBox<String> CategoryCombo;
    @FXML
    private TextField productIdField;
    @FXML
    private TextField quantityField;
    @FXML
    private Label saleStatusLabel;
    @FXML
    private Label cartLabelStatus;

    private ObservableList<Model> allProducts = FXCollections.observableArrayList();
    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // Load product data from DB
        dbpackage.Dbconnectivity dao = new dbpackage.Dbconnectivity();
        try 
        {
            ArrayList<Model> datas = dao.retrieveProducts();
            allProducts.setAll(datas);
            productTable.setItems(allProducts);
            loadCategories();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmployeeDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Set cart table items
        cartTable.setItems(cartItems);
    }

    private void loadCategories() {
        HashSet<String> categories = new HashSet<String>();
        for (Model p : allProducts) {
            categories.add(p.getCategory());
        }

        CategoryCombo.getItems().clear();
        CategoryCombo.getItems().add("All Categories");
        CategoryCombo.getItems().addAll(categories);
        CategoryCombo.setValue("All Categories");

        CategoryCombo.setOnAction(e -> filterByCategory());
    }

    private void filterByCategory() 
    {
        String selectedCategory = CategoryCombo.getValue();
        if ("All Categories".equals(selectedCategory)) 
        {
            productTable.setItems(allProducts);
        }
        else 
        {
            ObservableList<Model> filteredList = FXCollections.observableArrayList();
            for (Model p : allProducts) {
                if (p.getCategory().equalsIgnoreCase(selectedCategory)) {
                    filteredList.add(p);
                }
            }
            productTable.setItems(filteredList);
        }
    }

    @FXML
    private void handleaddToCart(ActionEvent event) {
        String productIdText = productIdField.getText().trim();
        String qtyText = quantityField.getText().trim();

        if (productIdText.isEmpty() || qtyText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Enter product ID and quantity.");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdText);
            int qty = Integer.parseInt(qtyText);

            Model selectedProduct = null;
            for (Model p : allProducts) {
                if (p.getId() == productId) {
                    selectedProduct = p;
                    break;
                }
            }

            if (selectedProduct == null) 
            {
                showAlert(Alert.AlertType.ERROR, "Product Not Found", "Product ID not found.");
                return;
            }

            if (qty <= 0) 
            {
                showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Quantity must be greater than 0.");
                return;
            }

            if (qty > selectedProduct.getStock()) {
                //cartLabelStatus.setText("❌ Not enough stock.");
                showAlert(Alert.AlertType.ERROR, "Stock Error", "Not enough stock available.");
                return;
            }

            // Check if product already exists in cart
            boolean found = false;
            for (CartItem item : cartItems) {
                if (item.getProductId() == productId) {
                    item.setQuantity(item.getQuantity() + qty);
                    found = true;
                    break;
                }
            }

            if (!found) {
                cartItems.add(new CartItem(
                        selectedProduct.getId(),
                        selectedProduct.getName(),
                        selectedProduct.getPrice(),
                        qty
                ));
            }

            cartTable.refresh();
            //cartLabelStatus.setText("🛒 Added to cart: " + selectedProduct.getName() + " x" + qty);
            showAlert(Alert.AlertType.INFORMATION, "Added to Cart", selectedProduct.getName() + " x" + qty + " added to cart.");

            quantityField.clear();
            productIdField.clear();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "❌ Invalid number format.");
        }
    }

    @FXML
    private void handleConfirmSalebtn(ActionEvent event) {
        if (cartItems.isEmpty()) {
            //saleStatusLabel.setText("❌ Cart is empty. Add items first.");
            showAlert(Alert.AlertType.WARNING, "Cart Empty", "❌ Cart is empty. Add items first.");

            return;
        }

        try {
            dbpackage.Dbconnectivity dao = new dbpackage.Dbconnectivity();
            boolean allSuccessful = true;

            for (CartItem item : cartItems) {
                Model product = null;
                for (Model p : allProducts) {
                    if (p.getId() == item.getProductId()) {
                        product = p;
                        break;
                    }
                }

                if (product == null) continue;

                if (item.getQuantity() > product.getStock()) {
                    //saleStatusLabel.setText("❌ Not enough stock for: " + product.getName());
                    showAlert(Alert.AlertType.ERROR, "Stock Error", "❌ Not enough stock for: " + product.getName());
                    allSuccessful = false;
                    continue;
                }

                double totalAmount = item.getTotal();
                boolean saleInserted = dao.insertSale(product.getId(), product.getName(), item.getQuantity(), totalAmount);
                boolean stockUpdated = dao.updateProductQuantity(product.getId(), item.getQuantity());

                if (saleInserted && stockUpdated) 
                {
                    product.setStock(product.getStock() - item.getQuantity());
                }
                else
                {
                    allSuccessful = false;
                }
            }

            if (allSuccessful) {
               // saleStatusLabel.setText("✅ Sale confirmed for all items!");
                showAlert(Alert.AlertType.INFORMATION, "Sale Confirmed", "✅ Sale confirmed for all items!");

            } 
            else 
            {
               // saleStatusLabel.setText("⚠️ Some items failed. Please check stock.");
                showAlert(Alert.AlertType.WARNING, "Sale Warning", "⚠️ Some items failed. Please check stock.");

            }

            cartItems.clear();
            cartTable.refresh();
            productTable.refresh();
            productIdField.clear();
            quantityField.clear();
            //cartLabelStatus.setText("🛒 Cart emptied after sale.");
            showAlert(Alert.AlertType.INFORMATION, "Cart Cleared", "🛒 Cart emptied after sale.");


        } catch (Exception e) {
            System.out.println(e);
            //saleStatusLabel.setText("❌ Error: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "❌ Error: " + e.getMessage());

        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) 
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header
        alert.setContentText(message);
        alert.showAndWait();
    }
}
