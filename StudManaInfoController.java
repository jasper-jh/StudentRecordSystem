package studentrecordsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;

public class StudManaInfoController {

    @FXML private TextField studentIdField, lastNameField, firstNameField, guardianNameField, guardianEmailField;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> studentIdColumn, lastNameColumn, firstNameColumn, guardianNameColumn, guardianEmailColumn;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        studentIdColumn.setCellValueFactory(data -> data.getValue().studentIdProperty());
        lastNameColumn.setCellValueFactory(data -> data.getValue().lastNameProperty());
        firstNameColumn.setCellValueFactory(data -> data.getValue().firstNameProperty());
        guardianNameColumn.setCellValueFactory(data -> data.getValue().guardianNameProperty());
        guardianEmailColumn.setCellValueFactory(data -> data.getValue().guardianEmailProperty());

        studentTable.setItems(studentList);

        studentIdField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d{0,2}-?\\d{0,4}")) {
                studentIdField.setText(oldVal);
            } else if (newVal.length() == 2 && !newVal.contains("-")) {
                studentIdField.setText(newVal + "-");
            }
        });

        loadStudentsFromDatabase();
    }

    private void loadStudentsFromDatabase() {
        studentList.clear();
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM studentrecords")) {

            while (rs.next()) {
                studentList.add(new Student(
                    rs.getString("Student_ID"),
                    rs.getString("LastName"),
                    rs.getString("FirstName"),
                    rs.getString("GuardianName"),
                    rs.getString("GuardianEmail")
                ));
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Failed to load data: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddStudent(ActionEvent event) {
        if (fieldsEmpty()) {
            showAlert(AlertType.ERROR, "All fields must be filled.");
            return;
        }

        String checkSql = "SELECT * FROM studentrecords WHERE Student_ID = ?";
        String insertSql = "INSERT INTO studentrecords (Student_ID, LastName, FirstName, GuardianName, GuardianEmail) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, studentIdField.getText());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                showAlert(AlertType.WARNING, "Student ID already exists.");
                return;
            }

            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, studentIdField.getText());
            insertStmt.setString(2, lastNameField.getText());
            insertStmt.setString(3, firstNameField.getText());
            insertStmt.setString(4, guardianNameField.getText());
            insertStmt.setString(5, guardianEmailField.getText());
            insertStmt.executeUpdate();

            showAlert(AlertType.INFORMATION, "Student added successfully!");
            clearFields();
            loadStudentsFromDatabase();

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Add failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateStudent(ActionEvent event) {
        String studentId = studentIdField.getText();
        if (studentId.isEmpty()) {
            showAlert(AlertType.ERROR, "Student ID is required to update.");
            return;
        }

        StringBuilder changes = new StringBuilder("Are you sure you want to update the following?\n");
        boolean hasChanges = false;

        if (!lastNameField.getText().isEmpty()) {
            changes.append("Last Name -> ").append(lastNameField.getText()).append("\n");
            hasChanges = true;
        }
        if (!firstNameField.getText().isEmpty()) {
            changes.append("First Name -> ").append(firstNameField.getText()).append("\n");
            hasChanges = true;
        }
        if (!guardianNameField.getText().isEmpty()) {
            changes.append("Guardian Name -> ").append(guardianNameField.getText()).append("\n");
            hasChanges = true;
        }
        if (!guardianEmailField.getText().isEmpty()) {
            changes.append("Guardian Email -> ").append(guardianEmailField.getText()).append("\n");
            hasChanges = true;
        }

        if (!hasChanges) {
            showAlert(AlertType.WARNING, "Enter at least one field to update.");
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION, changes.toString(), ButtonType.OK, ButtonType.CANCEL);
        confirm.setTitle("Confirm Update");
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.OK) {
            try (Connection conn = DatabaseConnection.connect()) {
                if (!lastNameField.getText().isEmpty())
                    updateField(conn, "LastName", lastNameField.getText(), studentId);
                if (!firstNameField.getText().isEmpty())
                    updateField(conn, "FirstName", firstNameField.getText(), studentId);
                if (!guardianNameField.getText().isEmpty())
                    updateField(conn, "GuardianName", guardianNameField.getText(), studentId);
                if (!guardianEmailField.getText().isEmpty())
                    updateField(conn, "GuardianEmail", guardianEmailField.getText(), studentId);

                showAlert(AlertType.INFORMATION, "Student updated successfully.");
                clearFields();
                loadStudentsFromDatabase();
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Update failed: " + e.getMessage());
            }
        }
    }

    private void updateField(Connection conn, String column, String value, String studentId) throws SQLException {
        String sql = "UPDATE studentrecords SET " + column + " = ? WHERE Student_ID = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, value);
        pstmt.setString(2, studentId);
        pstmt.executeUpdate();
    }

    @FXML
    private void handleDeleteStudent(ActionEvent event) {
        String studentId = studentIdField.getText();
        if (studentId.isEmpty()) {
            showAlert(AlertType.ERROR, "Student ID is required to delete.");
            return;
        }

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM studentrecords WHERE Student_ID = ?")) {

            pstmt.setString(1, studentId);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                showAlert(AlertType.INFORMATION, "Student deleted.");
                clearFields();
                loadStudentsFromDatabase();
            } else {
                showAlert(AlertType.WARNING, "Student ID not found.");
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Delete failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        ((Stage) studentIdField.getScene().getWindow()).close();
    }

    private boolean fieldsEmpty() {
        return studentIdField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
               firstNameField.getText().isEmpty() || guardianNameField.getText().isEmpty() ||
               guardianEmailField.getText().isEmpty();
    }

    private void clearFields() {
        studentIdField.clear();
        lastNameField.clear();
        firstNameField.clear();
        guardianNameField.clear();
        guardianEmailField.clear();
    }

    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(type.name());
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
