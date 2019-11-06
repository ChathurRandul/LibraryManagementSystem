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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.CatalogueTM;
import util.IssueBookTM;
import util.ReturnBookTM;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class ReturnBookFormController {
    public ImageView btnHome;
    public AnchorPane returnPane;
    public JFXTextField txtIssueDate;
    public JFXTextField txtBookTitle;
    public JFXTextField txtMemberId;
    public JFXTextField txtMemberName;
    public JFXTextField txtFines;
    public JFXButton btnReturn;
    public JFXTextField txtIssueId;
    public TextField txtSearch;
    public TableView<ReturnBookTM> tblReturn;
    public JFXTextField txtReturnDate;
    public JFXTextField txtBookId;
    public Text txtOverdueDates;
    public Text txtDueDate;

    private Connection connection;
    private PreparedStatement psForSelect;
    private PreparedStatement psForInsert;
    private PreparedStatement psForUpdateBookStatus;
    private PreparedStatement psForUpdateReturnStatus;

    public void initialize() throws ClassNotFoundException, SQLException {

        disableFields();

        tblReturn.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("bookId"));
        tblReturn.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        tblReturn.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("issueId"));
        tblReturn.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        tblReturn.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("memberId"));
        tblReturn.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("memberName"));
        tblReturn.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        tblReturn.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("overdueFines"));

        Class.forName("com.mysql.jdbc.Driver");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "1234");
            psForSelect = connection.prepareStatement("SELECT * FROM BookReturn WHERE bookid LIKE ? OR issuedate LIKE ? OR " +
                    "issueid LIKE ? OR booktitle LIKE ? OR memberid LIKE ? OR membername LIKE ? OR returndate LIKE ? OR  fine LIKE ?");
            psForInsert = connection.prepareStatement("INSERT INTO BookReturn VALUES (?,?,?,?,?,?,?,?)");
            psForUpdateBookStatus = connection.prepareStatement("UPDATE Book SET status=? WHERE bookid=?");
            psForUpdateReturnStatus = connection.prepareStatement("UPDATE BookIssue SET returnstatus=? WHERE issueid=?");
            loadAllReturns();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadAllReturns();

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    loadAllReturns();
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

    private void loadAllReturns() throws SQLException {
        tblReturn.getItems().clear();
        psForSelect.setString(1, "%" + txtSearch.getText() + "%");
        psForSelect.setString(2, "%" + txtSearch.getText() + "%");
        psForSelect.setString(3, "%" + txtSearch.getText() + "%");
        psForSelect.setString(4, "%" + txtSearch.getText() + "%");
        psForSelect.setString(5, "%" + txtSearch.getText() + "%");
        psForSelect.setString(6, "%" + txtSearch.getText() + "%");
        psForSelect.setString(7, "%" + txtSearch.getText() + "%");
        psForSelect.setString(8, "%" + txtSearch.getText() + "%");
        ResultSet resultSet = psForSelect.executeQuery();

        ObservableList<ReturnBookTM> returns = tblReturn.getItems();

        while (resultSet.next()) {
            String bookid = resultSet.getString("bookid");
            String issuedate = resultSet.getString("issuedate");
            String issueid = resultSet.getString("issueid");
            String booktitle = resultSet.getString("booktitle");
            String memberid = resultSet.getString("memberid");
            String membername = resultSet.getString("membername");
            String returndate = resultSet.getString("returndate");
            Double fine = Double.parseDouble(resultSet.getString("fine"));
            ReturnBookTM returnBookTM = new ReturnBookTM(bookid,issuedate,issueid,booktitle,memberid,membername,returndate,fine);
            returns.add(returnBookTM);
        }
    }

    public void btnReturn_OnAction(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Return this book?", ButtonType.NO,ButtonType.YES);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES){
            psForInsert.setString(1, txtBookId.getText());
            psForInsert.setString(2, txtIssueDate.getText());
            psForInsert.setString(3, txtIssueId.getText());
            psForInsert.setString(4, txtBookTitle.getText());
            psForInsert.setString(5, txtMemberId.getText());
            psForInsert.setString(6, txtMemberName.getText());
            psForInsert.setString(7, txtReturnDate.getText());
            psForInsert.setString(8, txtFines.getText());
            boolean returned = psForInsert.executeUpdate() > 0;
            if (returned){
                loadAllReturns();
                disableFields();
                txtDueDate.setText("");
                txtOverdueDates.setText("");
                psForUpdateBookStatus.setString(1, "Available");
                psForUpdateBookStatus.setString(2, txtBookId.getText());
                psForUpdateBookStatus.executeUpdate();
                psForUpdateReturnStatus.setString(1, "Returned");
                psForUpdateReturnStatus.setString(2, txtIssueId.getText());
                psForUpdateReturnStatus.executeUpdate();
                new Alert(Alert.AlertType.INFORMATION, "Book returned!").showAndWait();
            } else {
                new Alert(Alert.AlertType.ERROR, "Return failed!").showAndWait();
            }
        }
    }

    public void initializeForReturnBookForm(String bookId, String issueDate, String issueId, String bookTitle, String memberId, String memberName) {
        txtBookId.setText(bookId);
        txtIssueDate.setText(issueDate);
        txtIssueId.setText(issueId);
        txtBookTitle.setText(bookTitle);
        txtMemberId.setText(memberId);
        txtMemberName.setText(memberName);
        txtReturnDate.setText(LocalDate.now()+"");
        enableFields();
        calculateOverdueFines();
    }

    private void disableFields() {
        txtBookId.setDisable(true);
        txtIssueDate.setDisable(true);
        txtIssueId.setDisable(true);
        txtBookTitle.setDisable(true);
        txtMemberId.setDisable(true);
        txtMemberName.setDisable(true);
        txtReturnDate.setDisable(true);
        txtFines.setDisable(true);
        btnReturn.setDisable(true);
    }

    private void enableFields() {
        txtBookId.setDisable(false);
        txtIssueDate.setDisable(false);
        txtIssueId.setDisable(false);
        txtBookTitle.setDisable(false);
        txtMemberId.setDisable(false);
        txtMemberName.setDisable(false);
        txtReturnDate.setDisable(false);
        txtFines.setDisable(false);
        btnReturn.setDisable(false);
    }

    private void calculateOverdueFines(){
        LocalDate issueDate = LocalDate.parse(txtIssueDate.getText());
        LocalDate returnDate = LocalDate.parse(txtReturnDate.getText());
        LocalDate dueDate = issueDate.plusDays(14);

        txtDueDate.setText("Due Date : "+dueDate.toString());

        int duration = (int) ChronoUnit.DAYS.between(issueDate, returnDate);
        if (duration > 14) {
            int overdueDate = duration-14;
            double fine = overdueDate*10;
            txtFines.setText(String.format("%.2f",fine));
            txtOverdueDates.setText("Overdue Dates : "+overdueDate);
        } else {
            txtFines.setText("0.00");
            txtOverdueDates.setText("Overdue Dates : 0");
        }
    }

    public void btnHome_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/DashboardForm.fxml");
        Parent parent = FXMLLoader.load(resource);
        Scene scene = new Scene(parent);
        Stage primaryStage = (Stage) (this.returnPane.getScene().getWindow());
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
}
