package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
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
import util.IssueBookTM;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;

@SuppressWarnings("DuplicatedCode")
public class IssueBookFormController {
    public ImageView btnHome;
    public AnchorPane issuePane;
    public JFXTextField txtDate;
    public JFXTextField txtIssueId;
    public JFXTextField txtMemberName;
    public JFXTextField txtBookTitle;
    public JFXButton btnIssue;
    public TableView<IssueBookTM> tblIssue;
    public TextField txtSearch;
    public JFXTextField txtBookId;
    public JFXComboBox cmbMemberId;

    private Connection connection;
    private PreparedStatement psForSelect;
    private PreparedStatement psForInsert;
    private PreparedStatement psForSelectMembers;
    private PreparedStatement psForSelectAllIssues;
    private PreparedStatement psForUpdateStatus;

    public void initialize() throws ClassNotFoundException, SQLException {

        disableFields();

        //Add data to table
        tblIssue.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        tblIssue.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("issueId"));
        tblIssue.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("memberId"));
        tblIssue.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("memberName"));
        tblIssue.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("bookId"));
        tblIssue.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        tblIssue.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("returnStatus"));

        Class.forName("com.mysql.jdbc.Driver");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
            psForSelect = connection.prepareStatement("SELECT * FROM BookIssue WHERE issuedate LIKE ? OR issueid LIKE ? OR " +
                    "memberid LIKE ? OR membername LIKE ? OR bookid LIKE ? OR booktitle LIKE ? OR returnstatus LIKE ?");
            psForInsert = connection.prepareStatement("INSERT INTO BookIssue VALUES (?,?,?,?,?,?,?)");
            psForSelectAllIssues = connection.prepareStatement("SELECT * FROM BookIssue");
            psForSelectMembers = connection.prepareStatement("SELECT * FROM LibraryMember");
            psForUpdateStatus = connection.prepareStatement("UPDATE Book SET status=? WHERE bookid=?");
            loadAllIssues();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadAllMembers();

        cmbMemberId.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                String selectedMemberID = cmbMemberId.getSelectionModel().getSelectedItem().toString();
                try {
                    ObservableList<String> memberId = FXCollections.observableArrayList();
                    ResultSet rst = psForSelectMembers.executeQuery();
                    while (rst.next()) {
                        String mid = rst.getString(1);
                        String fname = rst.getString(2);
                        String lname = rst.getString(3);
                        memberId.add(mid);
                        for (String s : memberId) {
                            if (mid.equals(selectedMemberID)) {
                                txtMemberName.setText(fname + " " + lname);
                                break;
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                // catch
            }
        });

        //Search Issued Books
        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    loadAllIssues();
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

    private void loadAllIssues() throws SQLException {
        tblIssue.getItems().clear();
        psForSelect.setString(1, "%" + txtSearch.getText() + "%");
        psForSelect.setString(2, "%" + txtSearch.getText() + "%");
        psForSelect.setString(3, "%" + txtSearch.getText() + "%");
        psForSelect.setString(4, "%" + txtSearch.getText() + "%");
        psForSelect.setString(5, "%" + txtSearch.getText() + "%");
        psForSelect.setString(6, "%" + txtSearch.getText() + "%");
        psForSelect.setString(7, "%" + txtSearch.getText() + "%");
        ResultSet resultSet = psForSelect.executeQuery();

        ObservableList<IssueBookTM> issues = tblIssue.getItems();

        while (resultSet.next()) {
            String issuedate = resultSet.getString("issuedate");
            String issueid = resultSet.getString("issueid");
            String memberid = resultSet.getString("memberid");
            String membername = resultSet.getString("membername");
            String bookid = resultSet.getString("bookid");
            String booktitle = resultSet.getString("booktitle");
            String returnstatus = resultSet.getString("returnstatus");
            IssueBookTM issueBookTM = new IssueBookTM(issuedate, issueid, memberid, membername, bookid, booktitle, returnstatus);
            issues.add(issueBookTM);
        }
    }

    private void loadAllMembers() throws SQLException {
        ResultSet resultSet = psForSelectMembers.executeQuery();
        while (resultSet.next()) {
            String mid = resultSet.getString(1);
            ObservableList<String> members = cmbMemberId.getItems();
            members.add(mid);
        }
    }

    public void btnHome_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/DashboardForm.fxml");
        Parent parent = FXMLLoader.load(resource);
        Scene scene = new Scene(parent);
        Stage primaryStage = (Stage) (this.issuePane.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Dashboard");
        primaryStage.setResizable(false);
    }

    public void btnHome_OnMouseEntered(MouseEvent mouseEvent) {
        Tooltip.install(btnHome, new Tooltip("Home"));
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
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
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();
        }
    }

    public void btnIssue_OnAction(ActionEvent actionEvent) throws SQLException {
        String memberId = cmbMemberId.getSelectionModel().getSelectedItem().toString();

        ObservableList<IssueBookTM> issueBookTMS = FXCollections.observableArrayList();
        ResultSet resultSet = psForSelectAllIssues.executeQuery();

        while (resultSet.next()) {
            System.out.println(resultSet.getString("memberid"));
            if (resultSet.getString("memberid").equals(memberId) && resultSet.getString("returnstatus").equals("Pending")) {
                new Alert(Alert.AlertType.ERROR, "This member has already borrowed a book!", ButtonType.OK).showAndWait();
                return;
            }
        }
        psForInsert.setString(1, txtDate.getText());
        psForInsert.setString(2, txtIssueId.getText());
        psForInsert.setString(3, cmbMemberId.getSelectionModel().getSelectedItem().toString());
        psForInsert.setString(4, txtMemberName.getText());
        psForInsert.setString(5, txtBookId.getText());
        psForInsert.setString(6, txtBookTitle.getText());
        psForInsert.setString(7, "Pending");
        boolean inserted = psForInsert.executeUpdate() > 0;
        if (inserted) {
            loadAllIssues();
            disableFields();
            psForUpdateStatus.setString(1, "Not Available");
            psForUpdateStatus.setString(2, txtBookId.getText());
            psForUpdateStatus.executeUpdate();
            new Alert(Alert.AlertType.INFORMATION, "Book issued!").showAndWait();
        } else {
            new Alert(Alert.AlertType.ERROR, "Issue failed!").showAndWait();
        }
    }

    public void initializeForIssueBookForm(String bookId, String bookTitle) throws SQLException {
        txtDate.setText(LocalDate.now() + "");
        txtBookId.setText(bookId);
        txtBookTitle.setText(bookTitle);
        enableFields();
        generateId();
    }

    public void generateId() throws SQLException {
        // Generate the new issue id
        int maxIssueId = 0;
        ObservableList<String> iId = FXCollections.observableArrayList();
        ResultSet resultSet = psForSelect.executeQuery();
        while (resultSet.next()) {
            String iid = resultSet.getString("issueid");
            iId.add(iid);
            for (String s : iId) {
                int issueId = Integer.parseInt(s.replace("IB", ""));
                if (issueId > maxIssueId) {
                    maxIssueId = issueId;
                }
            }
            maxIssueId++;
            if (maxIssueId < 10) {
                txtIssueId.setText("IB00" + maxIssueId);
            } else if (maxIssueId < 100) {
                txtIssueId.setText("IB0" + maxIssueId);
            } else {
                txtIssueId.setText("IB" + maxIssueId);
            }
        }
    }

    public void disableFields() {
        txtIssueId.setDisable(true);
        cmbMemberId.setDisable(true);
        txtMemberName.setDisable(true);
        txtBookId.setDisable(true);
        txtBookTitle.setDisable(true);
        btnIssue.setDisable(true);
    }

    public void enableFields() {
        txtIssueId.setDisable(false);
        cmbMemberId.setDisable(false);
        txtMemberName.setDisable(false);
        txtBookId.setDisable(false);
        txtBookTitle.setDisable(false);
        btnIssue.setDisable(false);
    }

    public void clearFields() {
        txtIssueId.clear();
        //cmbMemberId.getSelectionModel().clearSelection();
        cmbMemberId.getSelectionModel().clearSelection();
        txtMemberName.clear();
        txtBookId.clear();
        txtBookTitle.clear();
    }

    public void tblIssue_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        IssueBookTM selectedItem = tblIssue.getSelectionModel().getSelectedItem();
        String returnStatus = selectedItem.getReturnStatus();

        if (mouseEvent.getClickCount() == 2) {
            if (returnStatus.equals("Returned")) {
                new Alert(Alert.AlertType.ERROR, "Book is already returned!", ButtonType.OK).showAndWait();
            } else {
                URL url = this.getClass().getResource("/view/ReturnBookForm.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(url);
                Parent parent = fxmlLoader.load();
                Scene returnBookFormScene = new Scene(parent);
                Stage stage = new Stage();
                stage.setScene(returnBookFormScene);
                stage.centerOnScreen();
                stage.setTitle("Return Book");
                stage.setResizable(false);

                ReturnBookFormController returnBookFormController = fxmlLoader.getController();
                IssueBookTM selectedBook = tblIssue.getSelectionModel().getSelectedItem();
                returnBookFormController.initializeForReturnBookForm(selectedBook.getBookId(), selectedBook.getIssueDate(), selectedBook.getIssueId(), selectedBook.getBookTitle(), selectedBook.getMemberId(), selectedBook.getMemberName());
                stage.show();
                closeWindow();
            }
        }
    }

    private void closeWindow() {
        // get a handle to the stage
        Stage stage = (Stage) this.issuePane.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
