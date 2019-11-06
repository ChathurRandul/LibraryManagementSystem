package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DB;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.MemberTM;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class MemberFormController {
    public AnchorPane memberPane;
    public ImageView btnHome;
    public JFXButton btnNewMember;
    public JFXTextField txtMemberId;
    public JFXTextField txtFirstName;
    public JFXTextField txtLastName;
    public JFXTextField txtAddress;
    public JFXTextField txtContactNo;
    public JFXTextField txtNIC;
    public JFXTextField txtEmail;
    public JFXButton btnAdd;
    public TableView<MemberTM> tblMembers;
    public JFXButton btnDelete;
    public TextField txtSearch;

    private Connection connection;
    private PreparedStatement psForSelect;
    private PreparedStatement psForInsert;
    private PreparedStatement psForUpdate;
    private PreparedStatement psForDelete;

    public void initialize() throws ClassNotFoundException {

        //Add data to table
        tblMembers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("memberId"));
        tblMembers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("memberFname"));
        tblMembers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("memberLname"));
        tblMembers.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("memberAddress"));
        tblMembers.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("memberContactNo"));
        tblMembers.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("memberNIC"));
        tblMembers.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("memberEmail"));

        Class.forName("com.mysql.jdbc.Driver");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","1234");
            psForSelect = connection.prepareStatement("SELECT * FROM LibraryMember WHERE memberid LIKE ? OR firstname LIKE ? OR " +
                    "lastname LIKE ? OR address LIKE ? OR contactno LIKE ? OR nic LIKE ? OR email LIKE ?");
            psForInsert = connection.prepareStatement("INSERT INTO LibraryMember VALUES (?,?,?,?,?,?,?)");
            psForUpdate = connection.prepareStatement("UPDATE LibraryMember SET firstname=?,lastname=?,address=?,contactno=?,nic=?,email=? WHERE memberid=?");
            psForDelete = connection.prepareStatement("DELETE FROM LibraryMember WHERE memberid=?");
            loadAllMembers();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        txtMemberId.setDisable(true);
        txtFirstName.setDisable(true);
        txtLastName.setDisable(true);
        txtAddress.setDisable(true);
        txtContactNo.setDisable(true);
        txtNIC.setDisable(true);
        txtEmail.setDisable(true);
        btnAdd.setDisable(true);

        //Update Member
        tblMembers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MemberTM>() {
            @Override
            public void changed(ObservableValue<? extends MemberTM> observable, MemberTM oldValue, MemberTM newValue) {
                MemberTM selectedItem = tblMembers.getSelectionModel().getSelectedItem();

                if (selectedItem == null){
                    btnAdd.setText("Add");
                    btnDelete.setDisable(true);
                    return;
                }
                btnAdd.setText("Update");
                btnAdd.setDisable(false);
                btnDelete.setDisable(false);
                txtFirstName.setDisable(false);
                txtLastName.setDisable(false);
                txtAddress.setDisable(false);
                txtContactNo.setDisable(false);
                txtNIC.setDisable(false);
                txtEmail.setDisable(false);
                txtMemberId.setText(selectedItem.getMemberId());
                txtFirstName.setText(selectedItem.getMemberFname());
                txtLastName.setText(selectedItem.getMemberLname());
                txtAddress.setText(selectedItem.getMemberAddress());
                txtContactNo.setText(selectedItem.getMemberContactNo());
                txtNIC.setText(selectedItem.getMemberNIC());
                txtEmail.setText(selectedItem.getMemberEmail());
            }
        });

        //Search Member
        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    loadAllMembers();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (connection!=null){
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));
    }

    private void loadAllMembers() throws SQLException {
        tblMembers.getItems().clear();
        psForSelect.setString(1,"%" + txtSearch.getText() + "%");
        psForSelect.setString(2,"%" + txtSearch.getText() + "%");
        psForSelect.setString(3,"%" + txtSearch.getText() + "%");
        psForSelect.setString(4,"%" + txtSearch.getText() + "%" );
        psForSelect.setString(5,"%" + txtSearch.getText() + "%");
        psForSelect.setString(6,"%" + txtSearch.getText() + "%");
        psForSelect.setString(7,"%" + txtSearch.getText() + "%");
        ResultSet resultSet = psForSelect.executeQuery();

        ObservableList<MemberTM> members = tblMembers.getItems();

        while (resultSet.next()){
            String memberid = resultSet.getString("memberid");
            String fname = resultSet.getString("firstname");
            String lname = resultSet.getString("lastname");
            String address = resultSet.getString("address");
            String contactno = resultSet.getString("contactno");
            String nic = resultSet.getString("nic");
            String email = resultSet.getString("email");
            MemberTM memberTM = new MemberTM(memberid, fname, lname, address, contactno, nic, email);
            members.add(memberTM);

            btnDelete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Delete Member?",ButtonType.YES,ButtonType.NO);
                     Optional<ButtonType> buttonType = alert.showAndWait();
                     if (buttonType.get() == ButtonType.YES){
                         try {
                             psForDelete.setString(1, memberid);
                             boolean deleted = psForDelete.executeUpdate() > 0;
                             if (deleted){
                                 new Alert(Alert.AlertType.INFORMATION,"Member deleted");
                                 loadAllMembers();
                                 tblMembers.refresh();
                                 btnNewMember_OnAction(event);
                             } else {
                                 new Alert(Alert.AlertType.ERROR, "Failed to delete").show();
                             }
                         } catch (SQLException e) {
                             e.printStackTrace();
                         }
                     }
                }
            });
        }
    }

    public void btnHome_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/DashboardForm.fxml");
        Parent parent = FXMLLoader.load(resource);
        Scene scene = new Scene(parent);
        Stage primaryStage = (Stage) (this.memberPane.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Dashboard");
        primaryStage.setResizable(false);
    }

    public void btnHome_OnMouseEntered(MouseEvent mouseEvent) {
        Tooltip.install(btnHome, new Tooltip("Home"));
        if (mouseEvent.getSource() instanceof ImageView){
            ImageView icon = (ImageView) mouseEvent.getSource();

            ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.WHITE);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
        }
    }

    public void btnHome_OnMouseExited(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView){
            ImageView icon = (ImageView) mouseEvent.getSource();
            ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();
        }
    }

    public void btnNewMember_OnAction(ActionEvent actionEvent) throws SQLException {
        txtMemberId.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtAddress.clear();
        txtContactNo.clear();
        txtNIC.clear();
        txtEmail.clear();
        tblMembers.getSelectionModel().clearSelection();
        txtMemberId.setDisable(false);
        txtFirstName.setDisable(false);
        txtLastName.setDisable(false);
        txtAddress.setDisable(false);
        txtContactNo.setDisable(false);
        txtNIC.setDisable(false);
        txtEmail.setDisable(false);
        txtFirstName.requestFocus();
        btnAdd.setDisable(false);

        generateId();
    }

    private void generateId() throws SQLException {
        // Generate a new id
        int maxId = 0;
        ObservableList<String> memberId = FXCollections.observableArrayList();
        ResultSet resultSet = psForSelect.executeQuery();
        while(resultSet.next()){
            String mid = resultSet.getString("memberid");
            memberId.add(mid);
            for (String s : memberId) {
                int id = Integer.parseInt(s.replace("MBR", ""));
                if (id > maxId){
                    maxId = id;
                }
            }
            maxId = maxId + 1;
            String id = "";
            if (maxId < 10){
                id = "MBR00" + maxId;
            }else if (maxId < 100){
                id = "MBR0" + maxId;
            }else{
                id = "MBR" + maxId;
            }
            txtMemberId.setText(id);
        }
    }

    public void btnAdd_OnAction(ActionEvent actionEvent) throws SQLException {
        //Add Member
        if (btnAdd.getText().equals("Add")) {
            psForInsert.setString(1, txtMemberId.getText());
            psForInsert.setString(2, txtFirstName.getText());
            psForInsert.setString(3, txtLastName.getText());
            psForInsert.setString(4, txtAddress.getText());
            psForInsert.setString(5, txtContactNo.getText());
            psForInsert.setString(6, txtNIC.getText());
            psForInsert.setString(7, txtEmail.getText());
            if (psForInsert.executeLargeUpdate() > 0){
                new Alert(Alert.AlertType.INFORMATION,"Member added!").showAndWait();
                loadAllMembers();
                btnNewMember_OnAction(actionEvent);
                generateId();
            } else{
                new Alert(Alert.AlertType.ERROR,"Failed to add member!").showAndWait();
            }
        }
        // Update Member
        else {
            MemberTM selectedItem = tblMembers.getSelectionModel().getSelectedItem();
            psForUpdate.setString(1, txtFirstName.getText());
            psForUpdate.setString(2, txtLastName.getText());
            psForUpdate.setString(3, txtAddress.getText());
            psForUpdate.setString(4, txtContactNo.getText());
            psForUpdate.setString(5, txtNIC.getText());
            psForUpdate.setString(6, txtEmail.getText());
            psForUpdate.setString(7, txtMemberId.getText());
            boolean updated = psForUpdate.executeUpdate()>0;
            if (updated){
                new Alert(Alert.AlertType.INFORMATION, "Updated successfully").show();
                loadAllMembers();
                tblMembers.refresh();
                btnNewMember_OnAction(actionEvent);
                generateId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to updated").show();
            }
        }
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this member??",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES){

        }
    }

    public void validateFields(){

        String fname = txtFirstName.getText();
        boolean vFname = fname.matches("^\\D{1,15}$");
        String lname = txtLastName.getText();
        boolean vLname = lname.matches("^\\D{2,15}$");
        String address = txtAddress.getText();
        boolean vAddress = address.matches("^\\w{4,30}$");
        String contact = txtContactNo.getText();
        boolean vContact = contact.matches("^\\d{3}-\\d{7}$");
        String nic = txtNIC.getText();
        boolean vNIC = nic.matches("\\b\\d{9,11}[Vv]\\b");
        String email = txtEmail.getText();
        boolean vEmail = email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

        if (!vFname){
            new Alert(Alert.AlertType.ERROR, "Invalid First Name!", ButtonType.OK).showAndWait();
            txtFirstName.requestFocus();
        } else if (!vLname){
            new Alert(Alert.AlertType.ERROR, "Invalid Last Name!", ButtonType.OK).showAndWait();
            txtLastName.requestFocus();
        } else if (!vAddress){
            new Alert(Alert.AlertType.ERROR, "Invalid Address!", ButtonType.OK).showAndWait();
            txtAddress.requestFocus();
        } else if (!vContact){
            new Alert(Alert.AlertType.ERROR, "Invalid Contact Number!", ButtonType.OK).showAndWait();
            txtContactNo.requestFocus();
        }else if (!vNIC){
            new Alert(Alert.AlertType.ERROR, "Invalid NIC Number!", ButtonType.OK).showAndWait();
            txtNIC.requestFocus();
        } else if (!vEmail){
            new Alert(Alert.AlertType.ERROR, "Invalid Email!", ButtonType.OK).showAndWait();
            txtEmail.requestFocus();
        }
    }
}
