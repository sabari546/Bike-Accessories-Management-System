package javafxapplication13;

import Admin.EmpDetails;
import Admin.empDB;
import java.io.IOException;
import java.net.URL;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class loginSceneController implements Initializable {
    
    @FXML
    private Button button;
    @FXML
    private ComboBox<String> roleBox;
    @FXML
    private TextField UserPass;
    @FXML
    private TextField userName;
    @FXML
    private AnchorPane loginScene;
    
    
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        String uName = userName.getText();
        String uPass = UserPass.getText();
        String role = roleBox.getSelectionModel().getSelectedItem();

        empDB edb = new empDB();
        ArrayList<EmpDetails> getlist = edb.retriveEmps();

        boolean isValidUser = false;

        for (EmpDetails c : getlist) 
        {
            if (uName.equalsIgnoreCase(c.getUsername()) &&
                uPass.equals(c.getPassword()) &&
                role.equals(c.getRole())) 
            {

                isValidUser = true;

                Stage st = new Stage();
                Parent root;

                if (role.equalsIgnoreCase("Admin")) 
                {
                    root = FXMLLoader.load(getClass().getResource("/Admin/AdminArea.fxml"));
                }
                else
                {
                    root = FXMLLoader.load(getClass().getResource("/Employee/EmployeeDocument.fxml"));
                }

                Scene sc = new Scene(root);
                st.setScene(sc);
                st.show();

                loginScene.getScene().getWindow().hide();
                break; 
            }
        }

        if (!isValidUser) 
        {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setContentText("Invalid username, password, or role!");
            al.showAndWait();
        }
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        String ar[] = {"Admin", "Employee", "cashier", "mechanic",  "warehouse staff", "Front desk"};
        for(String obj: ar)
        {
            roleBox.getItems().add(obj);
        }
        roleBox.getSelectionModel().select(ar[0]);
        
        
    }    
    
}
