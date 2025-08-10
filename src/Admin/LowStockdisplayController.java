package Admin;

import Employee.Model;
import dbpackage.Dbconnectivity;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class LowStockdisplayController implements Initializable 
{
    @FXML
    private TableView<Model> lowStocktable;
    @FXML
    private TableColumn<Model, Integer> colid;
    @FXML
    private TableColumn<Model, String> colname;
    @FXML
    private TableColumn<Model, String> colCat;
    @FXML
    private TableColumn<Model, Integer> calPrice;
    @FXML
    private TableColumn<Model, Integer> colStock;

    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Dbconnectivity dao = new Dbconnectivity();
        try {
            ArrayList<Model> list =  dao.lowStockList();
            
            for(Model li: list)
            {
                lowStocktable.getItems().add(li);
            }
            
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }    
    
}
