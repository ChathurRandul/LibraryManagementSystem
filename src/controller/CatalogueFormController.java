package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import util.CatalogueTM;
import util.Category;
import util.MemberTM;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class CatalogueFormController {
    public AnchorPane cataloguePane;
    public ImageView btnHome;
    public JFXButton btnNewBook;
    public JFXTextField txtBookId;
    public JFXTextField txtAuthor;
    public JFXTextField txtPublisher;
    public JFXTextField txtPrice;
    public JFXTextField txtTitle;
    public JFXButton btnAdd;
    public JFXButton btnDelete;
    public TableView<CatalogueTM> tblBooks;
    public JFXComboBox cmbCategory;
    public TextField txtSearch;

    private Connection connection;
    private PreparedStatement psForSelect;
    private PreparedStatement psForInsert;
    private PreparedStatement psForUpdate;
    private PreparedStatement psForDelete;

    public void initialize() throws ClassNotFoundException {

        //Add data to table
        tblBooks.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("bookId"));
        tblBooks.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        tblBooks.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
        tblBooks.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("bookCategory"));
        tblBooks.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("bookPublisher"));
        tblBooks.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("bookPrice"));
        tblBooks.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("bookStatus"));

        txtBookId.setDisable(true);
        txtTitle.setDisable(true);
        txtAuthor.setDisable(true);
        cmbCategory.setDisable(true);
        txtPublisher.setDisable(true);
        txtPrice.setDisable(true);
        btnAdd.setDisable(true);

        Class.forName("com.mysql.jdbc.Driver");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","1234");
            psForSelect = connection.prepareStatement("SELECT * FROM Book WHERE bookid LIKE ? OR title LIKE ? OR " +
                    "author LIKE ? OR category LIKE ? OR publisher LIKE ? OR price LIKE ? OR status LIKE ?");
            psForInsert = connection.prepareStatement("INSERT INTO Book VALUES (?,?,?,?,?,?,?)");
            psForUpdate = connection.prepareStatement("UPDATE Book SET title=?,author=?,category=?,publisher=?,price=? WHERE bookid=?");
            psForDelete = connection.prepareStatement("DELETE FROM Book WHERE bookid=?");
            loadAllBooks();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ObservableList<String> cmbCategoryItems = cmbCategory.getItems();
        for (Category category : DB.categories) {
            cmbCategoryItems.add(category.getCategory());
        }

        //Update Book
        tblBooks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CatalogueTM>() {
            @Override
            public void changed(ObservableValue<? extends CatalogueTM> observable, CatalogueTM oldValue, CatalogueTM newValue) {
                CatalogueTM selectedItem = tblBooks.getSelectionModel().getSelectedItem();

                if (selectedItem == null){
                    btnAdd.setText("Add");
                    btnDelete.setDisable(true);
                    return;
                }

                btnAdd.setText("Update");
                btnAdd.setDisable(false);
                btnDelete.setDisable(false);
                txtTitle.setDisable(false);
                txtAuthor.setDisable(false);
                cmbCategory.setDisable(false);
                txtPublisher.setDisable(false);
                txtPrice.setDisable(false);
                txtBookId.setText(selectedItem.getBookId());
                txtTitle.setText(selectedItem.getBookTitle());
                txtAuthor.setText(selectedItem.getBookAuthor());
                cmbCategory.getSelectionModel().select(selectedItem.getBookCategory());
                txtPublisher.setText(selectedItem.getBookPublisher());
                txtPrice.setText(selectedItem.getBookPrice()+"");
            }
        });

        //Search Book
        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    loadAllBooks();
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

    private void loadAllBooks() throws SQLException {
        tblBooks.getItems().clear();
        psForSelect.setString(1,"%" + txtSearch.getText() + "%");
        psForSelect.setString(2,"%" + txtSearch.getText() + "%");
        psForSelect.setString(3,"%" + txtSearch.getText() + "%");
        psForSelect.setString(4,"%" + txtSearch.getText() + "%" );
        psForSelect.setString(5,"%" + txtSearch.getText() + "%");
        psForSelect.setString(6,"%" + txtSearch.getText() + "%");
        psForSelect.setString(7,"%" + txtSearch.getText() + "%");
        ResultSet resultSet = psForSelect.executeQuery();

        ObservableList<CatalogueTM> books = tblBooks.getItems();

        while (resultSet.next()){
            String bookid = resultSet.getString("bookid");
            String title = resultSet.getString("title");
            String author = resultSet.getString("author");
            String category = resultSet.getString("category");
            String publisher = resultSet.getString("publisher");
            Double price = Double.parseDouble(resultSet.getString("price"));
            String status = resultSet.getString("status");
            CatalogueTM catalogueTM = new CatalogueTM(bookid,title,author,category,publisher,price,status);
            books.add(catalogueTM);

            btnDelete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Delete Book?",ButtonType.YES,ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.YES){
                        try {
                            psForDelete.setString(1, bookid);
                            boolean deleted = psForDelete.executeUpdate() > 0;
                            if (deleted){
                                new Alert(Alert.AlertType.INFORMATION,"Book deleted");
                                loadAllBooks();
                                tblBooks.refresh();
                                btnNewBook_OnAction(event);
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
        Stage primaryStage = (Stage) (this.cataloguePane.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Dashboard");
        primaryStage.setResizable(false);
    }

    private void closeWindow(){
        // get a handle to the stage
        Stage stage = (Stage) this.cataloguePane.getScene().getWindow();
        // do what you have to do
        stage.close();
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

    public void btnNewBook_OnAction(ActionEvent actionEvent) throws SQLException {
        txtBookId.clear();
        txtTitle.clear();
        txtAuthor.clear();
        cmbCategory.getSelectionModel().clearSelection();
        txtPublisher.clear();
        txtPrice.clear();
        tblBooks.getSelectionModel().clearSelection();
        txtBookId.setDisable(false);
        txtTitle.setDisable(false);
        txtAuthor.setDisable(false);
        cmbCategory.setDisable(false);
        txtPublisher.setDisable(false);
        txtPrice.setDisable(false);
        txtTitle.requestFocus();
        btnAdd.setDisable(false);

        // Generate a new id
        int maxId = 0;
        ObservableList<String> bookId = FXCollections.observableArrayList();
        ResultSet resultSet = psForSelect.executeQuery();
        while (resultSet.next()){
            String bid = resultSet.getString("bookid");
            bookId.add(bid);
            for (String s : bookId) {
                int id = Integer.parseInt(s.replace("BK", ""));
                if (id > maxId){
                    maxId = id;
                }
            }
            maxId = maxId + 1;
            String id = "";
            if (maxId < 10){
                id = "BK00" + maxId;
            }else if (maxId < 100){
                id = "BK0" + maxId;
            }else{
                id = "BK" + maxId;
            }
            txtBookId.setText(id);
        }
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {

    }

    public void tblBooks_OnMouseClicked(MouseEvent mouseEvent) throws IOException, SQLException {
        CatalogueTM selectedItem = tblBooks.getSelectionModel().getSelectedItem();
        //ObservableList<String> bookStatus = FXCollections.observableArrayList();
        String bookStatus = selectedItem.getBookStatus();

            if (mouseEvent.getClickCount() == 2) {

                if (bookStatus.equals("Available")) {

                URL resource = this.getClass().getResource("/view/IssueBookForm.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(resource);
                Parent root = fxmlLoader.load();
                Scene issueBookFormScene = new Scene(root);
                Stage secondaryStage = new Stage();
                secondaryStage.setScene(issueBookFormScene);
                secondaryStage.centerOnScreen();
                secondaryStage.setTitle("Issue Book");
                secondaryStage.setResizable(false);

                IssueBookFormController issueBookFormController = fxmlLoader.getController();
                CatalogueTM selectedBook = tblBooks.getSelectionModel().getSelectedItem();
                issueBookFormController.initializeForIssueBookForm(selectedBook.getBookId(), selectedBook.getBookTitle());
                secondaryStage.show();
                closeWindow();
                }   else {
                    new Alert(Alert.AlertType.ERROR, "Sorry, Book is already issued!", ButtonType.OK).showAndWait();
                }
         }
    }

    public void BtnAdd_OnAction(ActionEvent actionEvent) throws SQLException {
            //Add Book
            if (btnAdd.getText().equals("Add")) {
                psForInsert.setString(1, txtBookId.getText());
                psForInsert.setString(2, txtTitle.getText());
                psForInsert.setString(3, txtAuthor.getText());
                psForInsert.setString(4, cmbCategory.getSelectionModel().getSelectedItem().toString());
                psForInsert.setString(5, txtPublisher.getText());
                psForInsert.setString(6, txtPrice.getText());
                psForInsert.setString(7, "Available");
                if (psForInsert.executeLargeUpdate() > 0){
                    new Alert(Alert.AlertType.INFORMATION,"Book added!").showAndWait();
                    loadAllBooks();
                    btnNewBook_OnAction(actionEvent);
                } else{
                    new Alert(Alert.AlertType.ERROR,"Failed to add book!").showAndWait();
                }
            }
            // Update Book
            else {
                psForUpdate.setString(1, txtTitle.getText());
                psForUpdate.setString(2, txtAuthor.getText());
                psForUpdate.setString(3, cmbCategory.getSelectionModel().getSelectedItem().toString());
                psForUpdate.setString(4, txtPublisher.getText());
                psForUpdate.setString(5, txtPrice.getText());
                psForUpdate.setString(6, txtBookId.getText());
                boolean updated = psForUpdate.executeUpdate()>0;
                if (updated){
                    new Alert(Alert.AlertType.INFORMATION, "Updated successfully").show();
                    loadAllBooks();
                    tblBooks.refresh();
                    btnNewBook_OnAction(actionEvent);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to updated").show();
                }
            }
    }


    public boolean validateFields() {

        String title = txtTitle.getText();
        boolean vTitle = title.matches("^.{1,50}$");
        String author = txtAuthor.getText();
        boolean vAuthor = author.matches("^[A-Za-z ]{1,20}$");
        String publisher = txtPublisher.getText();
        boolean vPublisher = publisher.matches("^[A-Za-z ]{1,20}$");
        String price = txtPrice.getText();
        boolean vPrice = price.matches("(\\d+\\.\\d{2})");

            if (!vTitle) {
                new Alert(Alert.AlertType.ERROR, "Invalid Book Title!", ButtonType.OK).showAndWait();
                txtTitle.requestFocus();
            } else if (!vAuthor) {
                new Alert(Alert.AlertType.ERROR, "Invalid Author Name!", ButtonType.OK).showAndWait();
                txtAuthor.requestFocus();
            } else if (!vPublisher) {
                new Alert(Alert.AlertType.ERROR, "Invalid Publisher Name!", ButtonType.OK).showAndWait();
                txtPublisher.requestFocus();
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid Book Price!", ButtonType.OK).showAndWait();
                txtPrice.requestFocus();
            }
        return true;
    }
}
