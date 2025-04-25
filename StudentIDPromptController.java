package studentrecordsystem;

import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class StudentIDPromptController implements Initializable {

    @FXML
    private TextField studentIdField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Auto-format the Student ID as XX-XXXX while typing
        studentIdField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d{0,2}-?\\d{0,4}")) {
                studentIdField.setText(oldVal);
            } else if (newVal.length() == 2 && !newVal.contains("-")) {
                studentIdField.setText(newVal + "-");
            }
        });
    }

    @FXML
    private void handleSendButtonAction() {
        String studentId = studentIdField.getText().trim();

        // Validate Student ID format
        if (studentId.isEmpty() || !studentId.matches("\\d{2}-\\d{4}")) {
            showAlert("Input Error", "Please enter a valid Student ID (e.g., 24-1433).");
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT FirstName, LastName, GuardianName, GuardianEmail FROM studentrecords WHERE Student_ID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, studentId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String guardianName = rs.getString("GuardianName");
                String guardianEmail = rs.getString("GuardianEmail");

                String studentFullName = lastName + ", " + firstName;

                LocalDateTime now = LocalDateTime.now();
                String formattedDate = now.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
                String formattedTime = now.format(DateTimeFormatter.ofPattern("h:mm a"));

                String message = "Dear Mr/Ms " + guardianName + ",\n\n" +
                        "This is to notify that your child, " + studentFullName +
                        ", has arrived at Quezon City University at " + formattedTime + " on " + formattedDate + ".\n\n" +
                        "To ensure validity of this message, please contact your child through their cellular device. " +
                        "If your child does not possess a device, instead, contact their corresponding adviser for attendance.\n\n" +
                        "Thank you, and God bless.";

                // Send email
                boolean emailSent = EmailSender.sendEmail(guardianEmail, "Student Arrival Notification", message);

                if (!emailSent) {
                    showAlert("Email Error", "Failed to send the email. Please check your internet connection or email credentials.");
                    return;
                }

                // Save to log
                saveNotification(studentId, guardianEmail, message);

                studentIdField.clear();

                showAlert("Notification Sent", "Email sent to " + guardianEmail + " at " + formattedTime + " on " + formattedDate + ".");
            } else {
                showAlert("Student Not Found", "No student found with the ID: " + studentId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Something went wrong.\n" + e.getMessage());
        }
    }

    private void saveNotification(String studentId, String guardianEmail, String message) {
        String insertQuery = "INSERT INTO notification_tbl (student_id, data_sent, sent_at, date_sent, time_sent) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            LocalDateTime now = LocalDateTime.now();
            Date date = Date.valueOf(now.toLocalDate()); // SQL Date
            Time time = Time.valueOf(now.toLocalTime().withSecond(0).withNano(0)); // SQL Time

            stmt.setString(1, studentId);
            stmt.setString(2, message);
            stmt.setString(3, guardianEmail);
            stmt.setDate(4, date);
            stmt.setTime(5, time);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleExitButtonAction() {
        studentIdField.getScene().getWindow().hide();
    }
}
