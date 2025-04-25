package studentrecordsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StudentRecordSystem extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/studentrecordsystem/FXMLDocument.fxml"));

            // Set up the stage
            Scene scene = new Scene(root);
            stage.setTitle("Student Record System");
            stage.setScene(scene);
            stage.show();
            System.out.println("FXMLDocument.fxml loaded successfully!");
        } catch (Exception e) {
            // Print error details for debugging
            System.err.println("Error loading FXMLDocument.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
