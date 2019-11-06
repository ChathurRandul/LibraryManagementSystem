package controller;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;

public class DashboardFormController {
    public AnchorPane dashboardPane;
    public ImageView btnMembers;
    public ImageView btnCatalogue;
    public ImageView btnIssueBook;
    public ImageView btnReturnBook;

    public void btnMembers_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        dashboardNavigation("/view/MemberForm.fxml","Members");
    }

    public void btnCatalogue_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        dashboardNavigation("/view/CatalogueForm.fxml","Catalogue");
    }

    public void btnIssueBook_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        dashboardNavigation("/view/IssueBookForm.fxml","Issue Book");
    }

    public void btnReturnBook_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        dashboardNavigation("/view/ReturnBookForm.fxml","Return Book");
    }

    public void dashboardNavigation(String path, String title) throws IOException {
        URL resource = this.getClass().getResource(path);
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage)(this.dashboardPane.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
    }

    public void btnMembers_OnMouseEntered(MouseEvent mouseEvent) {
        Tooltip.install(btnMembers, new Tooltip("Members"));
        mouseEnterAnimation(mouseEvent);
    }

    public void btnMembers_OnMouseExited(MouseEvent mouseEvent) {
        mouseExitAnimation(mouseEvent);
    }

    public void btnCatalogue_OnMouseEntered(MouseEvent mouseEvent) {
        Tooltip.install(btnCatalogue, new Tooltip("Catalogue"));
        mouseEnterAnimation(mouseEvent);
    }

    public void btnCatalogue_OnMouseExited(MouseEvent mouseEvent) {
        mouseExitAnimation(mouseEvent);
    }

    public void btnIssueBook_OnMouseEntered(MouseEvent mouseEvent) {
        Tooltip.install(btnIssueBook, new Tooltip("Issue Book"));
        mouseEnterAnimation(mouseEvent);
    }

    public void btnIssueBook_OnMouseExited(MouseEvent mouseEvent) {
        mouseExitAnimation(mouseEvent);
    }

    public void btnReturnBook_OnMouseEntered(MouseEvent mouseEvent) {
        Tooltip.install(btnReturnBook, new Tooltip("Return Book"));
        mouseEnterAnimation(mouseEvent);
    }

    public void btnReturnBook_OnMouseExited(MouseEvent mouseEvent) {
        mouseExitAnimation(mouseEvent);
    }

    public void mouseEnterAnimation(MouseEvent mouseEvent) {
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

    public void mouseExitAnimation(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView){
            ImageView icon = (ImageView) mouseEvent.getSource();
            ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();
        }
    }
}
