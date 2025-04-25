package studentrecordsystem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

public class FXMLDocumentController implements Initializable {

    /**
     * Handles the "Enter Student ID" button click.
     * Opens the StudentIDPrompt.fxml scene in a new window.
     */
    @FXML
    private void handleButtonAction(ActionEvent event) {
        openNewStage("StudentIDPrompt.fxml", "Enter Student ID");
    }

    /**
     * Handles the "Add New Student" button click.
     * Opens the AddNewStudent.fxml scene in a new window.
     */
    @FXML
    private void handleAddNewStudentAction(ActionEvent event) {
        openNewStage("AddNewStudent.fxml", "Add New Student");
    }

    /**
     * Handles the "Exit" button click.
     * Closes the current stage.
     */
    @FXML
    private void handleExitButtonAction(ActionEvent event) {
        try {
            // Close the current stage
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            showAlert("Error", "Unable to close the window.");
        }
    }

    /**
     * Opens a new stage with the specified FXML file and window title.
     *
     * @param fxmlFile The name of the FXML file to load (e.g., "AddNewStudent.fxml").
     * @param title    The title for the new stage.
     */
    private void openNewStage(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Create and display a new stage
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the " + title + " screen. Please check the FXML file.");
        }
    }

    /**
     * Displays an alert dialog with the specified title and message.
     *
     * @param title   The title of the alert dialog.
     * @param message The message displayed in the alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization logic if needed
    }
}