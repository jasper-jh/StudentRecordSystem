package studentrecordsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NotifLogController {

    @FXML
    private TableView<Notification> notifTable;

    @FXML
    private TableColumn<Notification, Integer> logidColumn;

    @FXML
    private TableColumn<Notification, String> studentIdColumn;

    @FXML
    private TableColumn<Notification, String> guardianEmailColumn;

    @FXML
    private TableColumn<Notification, String> dateSentColumn;

    @FXML
    private TableColumn<Notification, String> timeSentColumn;

    @FXML
    private TableColumn<Notification, String> messageColumn;

    @FXML
    private TextField deleteField;

    private final ObservableList<Notification> notifList = FXCollections.observableArrayList();

    private final String DB_URL = "jdbc:mysql://localhost:3306/student_record_system";
    private final String DB_USER = "root";
    private final String DB_PASS = ""; // update if needed

    @FXML
    public void initialize() {
        logidColumn.setCellValueFactory(new PropertyValueFactory<>("logId"));
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        guardianEmailColumn.setCellValueFactory(new PropertyValueFactory<>("sentAt"));
        dateSentColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeSentColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));

        loadNotifications();
    }

    private void loadNotifications() {
        notifList.clear();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "SELECT * FROM notification_tbl ORDER BY log_id DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notification notif = new Notification(
                    rs.getInt("log_id"),
                    rs.getString("student_id"),
                    rs.getString("sent_at"),
                    rs.getString("date_sent"),
                    rs.getString("time_sent"),
                    rs.getString("data_sent")
                );
                notifList.add(notif);
            }

            notifTable.setItems(notifList);

        } catch (Exception e) {
            showAlert("Database Error", "Failed to load notifications.\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        String idText = deleteField.getText().trim();
        if (idText.isEmpty()) {
            showAlert("Input Error", "Please enter a Log ID.");
            return;
        }

        try {
            int logId = Integer.parseInt(idText);
            deleteNotification(logId);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Log ID must be a number.");
        }
    }

    private void deleteNotification(int notifId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "DELETE FROM notification_tbl WHERE notif_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, notifId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Notification deleted successfully.");
                loadNotifications(); // refresh table
                deleteField.clear();
            } else {
                showAlert("Not Found", "No notification found with Log ID: " + notifId);
            }

        } catch (Exception e) {
            showAlert("Database Error", "Error deleting notification.\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/studentrecordsystem/FXMLDocument.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Main Menu");
            stage.setScene(new Scene(loader.load()));
            stage.show();

            // Close current window
            Stage currentStage = (Stage) notifTable.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            showAlert("Navigation Error", "Unable to return to the main menu.\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
