package studentrecordsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

public class FXMLDocumentController {

    @FXML
    private void handleStudentIDPrompt(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/studentrecordsystem/StudentIDPrompt.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Notify Guardian");
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("StudentIDPrompt.fxml loaded successfully!");
        } catch (Exception e) {
            showAlert("Error Loading Scene", "Unable to load StudentIDPrompt.fxml\nError: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStudManaInfo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/studentrecordsystem/StudManaInfo.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Student Management Information");
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("StudManaInfo.fxml loaded successfully!");
        } catch (Exception e) {
            showAlert("Error Loading Scene", "Unable to load StudManaInfo.fxml\nError: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNotifLog(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/studentrecordsystem/NotifLog.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Notification Log");
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("NotifLog.fxml loaded successfully!");
        } catch (Exception e) {
            showAlert("Error Loading Scene", "Unable to load NotifLog.fxml\nError: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
