package studentrecordsystem;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AddNewStudentController implements Initializable {

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<Student, String> idColumn;

    @FXML
    private TableColumn<Student, String> lastNameColumn;

    @FXML
    private TableColumn<Student, String> firstNameColumn;

    @FXML
    private TableColumn<Student, String> parentNameColumn;

    @FXML
    private TableColumn<Student, String> parentEmailColumn;

    @FXML
    private TextField idField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField parentNameField;

    @FXML
    private TextField parentEmailField;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initializing AddNewStudentController...");
        
        // Bind TableView columns to Student class properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        parentNameColumn.setCellValueFactory(new PropertyValueFactory<>("parentName"));
        parentEmailColumn.setCellValueFactory(new PropertyValueFactory<>("parentEmail"));

        // Load students into the TableView
        loadStudents();
    }

    private void loadStudents() {
        System.out.println("Loading students...");
        studentList.clear();

        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT * FROM students";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                studentList.add(new Student(
                        rs.getString("Student_ID"),
                        rs.getString("Last_Name"),
                        rs.getString("First_Name"),
                        rs.getString("Parents_Name"),
                        rs.getString("Parents_Email")
                ));
            }

            studentTable.setItems(studentList);
            System.out.println("Students loaded successfully!");
        } catch (SQLException e) {
            showAlert("Error", "Failed to load students.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddButtonAction(ActionEvent event) {
        System.out.println("Handling Add Button Action...");
        String id = idField.getText();
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String parentName = parentNameField.getText();
        String parentEmail = parentEmailField.getText();

        if (!fieldsAreEmpty(id, lastName, firstName, parentName, parentEmail)) {
            try (Connection conn = DatabaseConnection.connect()) {
                String query = "INSERT INTO students (Student_ID, Last_Name, First_Name, Parents_Name, Parents_Email) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, id);
                stmt.setString(2, lastName);
                stmt.setString(3, firstName);
                stmt.setString(4, parentName);
                stmt.setString(5, parentEmail);
                stmt.executeUpdate();

                showAlert("Success", "Student added successfully!");
                loadStudents(); // Refresh TableView
            } catch (SQLException e) {
                showAlert("Error", "Failed to add student. Ensure the ID is unique.");
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "All fields are required.");
        }
    }

    @FXML
    private void handleUpdateButtonAction(ActionEvent event) {
        System.out.println("Handling Update Button Action...");
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null && !fieldsAreEmpty(
                lastNameField.getText(), 
                firstNameField.getText(), 
                parentNameField.getText(), 
                parentEmailField.getText())) {
            try (Connection conn = DatabaseConnection.connect()) {
                String query = "UPDATE students SET Last_Name = ?, First_Name = ?, Parents_Name = ?, Parents_Email = ? WHERE Student_ID = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, lastNameField.getText());
                stmt.setString(2, firstNameField.getText());
                stmt.setString(3, parentNameField.getText());
                stmt.setString(4, parentEmailField.getText());
                stmt.setString(5, selectedStudent.getId());
                stmt.executeUpdate();

                showAlert("Success", "Student updated successfully!");
                loadStudents(); // Refresh TableView
            } catch (SQLException e) {
                showAlert("Error", "Failed to update student.");
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "Please select a student and ensure all fields are filled.");
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        System.out.println("Handling Delete Button Action...");
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            try (Connection conn = DatabaseConnection.connect()) {
                String query = "DELETE FROM students WHERE Student_ID = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, selectedStudent.getId());
                stmt.executeUpdate();

                showAlert("Success", "Student deleted successfully!");
                loadStudents(); // Refresh TableView
            } catch (SQLException e) {
                showAlert("Error", "Failed to delete student.");
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "Please select a student to delete.");
        }
    }

    @FXML
    private void handleExitButtonAction(ActionEvent event) {
        System.out.println("Exiting...");
        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean fieldsAreEmpty(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}