package Admin;



import Employee.Model;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


public class ManageEmpController implements Initializable {
    @FXML
    private TableView<EmpDetails> EmpTable;
    @FXML
    private TableColumn<EmpDetails, Integer> empid;
    @FXML
    private TableColumn<EmpDetails, String> EmpName;
    @FXML
    private TableColumn<EmpDetails, String> empGender;
    @FXML
    private TableColumn<EmpDetails, String> empPh;
    @FXML
    private TableColumn<EmpDetails, String> empRole;
    @FXML
    private TableColumn<EmpDetails, String> empJoinDate;
    @FXML
    private TextField empInputName;
    @FXML
    private TextField empPhInput;
    @FXML
    private ComboBox<String> Emprolebox;
    @FXML
    private TableColumn<EmpDetails, String> empUsernam;
    @FXML
    private TableColumn<EmpDetails, String> emppassW;
    @FXML
    private TextField createpass;
    @FXML
    private TextField createUser;
    @FXML
    private Button adduser;
    @FXML
    private Button updateUserbtn;
    @FXML
    private Button deleteUserbtn;
    @FXML
    private Button clearbtn;
    @FXML
    private RadioButton femaleRadio;
    @FXML
    private RadioButton maleRadio;
    @FXML
    private DatePicker joinDatePicker;

    private ObservableList<Model> allemployees = FXCollections.observableArrayList();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        empDB db = new empDB();
        try {
            ArrayList<EmpDetails>list = db.retriveEmps();
            for(EmpDetails obj : list)
            {
                EmpTable.getItems().add(obj);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ManageEmpController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ManageEmpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        String ar[] = {"cashier", "mechanic",  "warehouse staff", "Front desk"};
        for(String obj : ar)
        {
            Emprolebox.getItems().add(obj);
        }
        Emprolebox.getSelectionModel().select(ar[0]);
        
        
    }    

    @FXML
    private void handleAddUSer(ActionEvent event) 
    {
        String ename = empInputName.getText().trim();
        String egender = ""; // Read from radio button
        if (maleRadio.isSelected()) 
        {
            femaleRadio.setSelected(false);
            egender = "Male";
        }
        else if (femaleRadio.isSelected())
        {
            maleRadio.setSelected(false);
            egender = "Female";
        }

        String phoneStr = empPhInput.getText();
        String role = Emprolebox.getSelectionModel().getSelectedItem();
        LocalDate localDate = joinDatePicker.getValue();
        

        String username = createUser.getText().trim();
        String password = createpass.getText().trim();
        
        if (ename.isEmpty() || egender.isEmpty() || phoneStr.isEmpty() ||
        role == null || localDate == null || username.isEmpty() || password.isEmpty())
        {        
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Input Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Please fill all fields before submitting!");
            errorAlert.showAndWait();
            return; // Stop the function here
        }

        try {
            
            Date sqlDate = Date.valueOf(localDate);
            
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bikers", "postgres", "1998");

            String sql = "INSERT INTO employees (empname, gender, phoneno, emprole, joindate, empusername, emppassword) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ename);
            stmt.setString(2, egender);
            stmt.setString(3, phoneStr);
            stmt.setString(4, role);
            stmt.setDate(5, sqlDate);
            stmt.setString(6, username);
            stmt.setString(7, password);

            stmt.executeUpdate();
            conn.close();
            
            Alert al = new Alert(Alert.AlertType.CONFIRMATION);
            al.setContentText("Employee added successfully!! /nNow proceed login");
            al.showAndWait();
            System.out.println("✅ Employee added to database!");
            
        } catch (Exception e) {
            System.out.println(e);
            
        }
    }


    @FXML
    private void handleUpdateUser(ActionEvent event) throws ClassNotFoundException, SQLException 
    {
        String ename = empInputName.getText();
        String egender = ""; // Read from radio button
        if (maleRadio.isSelected()) 
        {
            femaleRadio.setSelected(false);
            egender = "Male";
        }
        else if (femaleRadio.isSelected())
        {
            maleRadio.setSelected(false);
            egender = "Female";
        }

        String phone = empPhInput.getText();
        String role = Emprolebox.getSelectionModel().getSelectedItem();
        LocalDate localDate = joinDatePicker.getValue();
        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
        String username = createUser.getText();
        String password = createpass.getText();
        
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bikers", "postgres", "1998");
        String sql = "update employees set empname = ?, gender =?, phoneno= ?, emprole =?, joindate= ?, emppassword =?  where empusername =?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,ename);
        ps.setString(2, egender);
        ps.setString(3,phone);
        ps.setString(4, role);
        ps.setDate(5, sqlDate);
        ps.setString(6, password);
        ps.setString(7, username);
        
       int res =  ps.executeUpdate();
       conn.close();
       
       Alert al = new Alert(Alert.AlertType.CONFIRMATION);
       al.setContentText("updated user");
       al.showAndWait();
        
        
    }

    @FXML
    private void handledeleteUser(ActionEvent event) throws ClassNotFoundException, SQLException 
    {
        
        EmpDetails selectedEmp = EmpTable.getSelectionModel().getSelectedItem();
        
        if (selectedEmp == null)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a empployee to delete.");
            alert.showAndWait();
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete this employee detail..?");
        Optional<ButtonType> result = confirm.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK)
        {
        try {
            empDB empdb = new empDB();
            boolean deleted = empdb.deleteUserbyId(selectedEmp.getEmpid());

            if (deleted) {
                allemployees.remove(selectedEmp);
                EmpTable.refresh();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("employee deleted successfully.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete employee from database.");
                alert.showAndWait();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
        
//        String ename = empInputName.getText();
//        String egender = ""; // Read from radio button
//        if (maleRadio.isSelected()) 
//        {
//            femaleRadio.setSelected(false);
//            egender = "Male";
//        }
//        else if (femaleRadio.isSelected())
//        {
//            maleRadio.setSelected(false);
//            egender = "Female";
//        }
//
//        int phone = Integer.parseInt(empPhInput.getText());
//        String role = Emprolebox.getSelectionModel().getSelectedItem();
//        LocalDate localDate = joinDatePicker.getValue();
//        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
//        String username = createUser.getText();
//        String password = createpass.getText();
//        
//        Class.forName("org.postgresql.Driver");
//        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bikers", "postgres", "1998");
//        String sql = " DELETE FROM employees WHERE empusername = ?";
//        PreparedStatement ps = conn.prepareStatement(sql);
//         ps.setString(1, username);
//         ps.executeUpdate();
//         conn.close();
//       
//       Alert al = new Alert(Alert.AlertType.CONFIRMATION);
//       al.setContentText("Delated user Sucessfully!!!");
//       al.showAndWait();
    }

    @FXML
    private void handleclearemp(ActionEvent event) 
    {
        empInputName.clear();
        empPhInput.clear();
        createUser.clear();
        createpass.clear();
        joinDatePicker.setValue(null);
        Emprolebox.getSelectionModel().select(0);
        maleRadio.setSelected(false);
        femaleRadio.setSelected(false);
    }

    @FXML
    private void handlefemaleRadio(ActionEvent event) 
    {
        if(femaleRadio.isSelected())
        {
            maleRadio.setSelected(false);
        }
    }

    @FXML
    private void handleMaleRadio(ActionEvent event)
    {
        if(maleRadio.isSelected())
        {
            femaleRadio.setSelected(false);
        }
    }
    
}
