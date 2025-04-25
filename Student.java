package studentrecordsystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {

    private final StringProperty studentId;
    private final StringProperty lastName;
    private final StringProperty firstName;
    private final StringProperty guardianName;
    private final StringProperty guardianEmail;

    public Student(String studentId, String lastName, String firstName, String guardianName, String guardianEmail) {
        this.studentId = new SimpleStringProperty(studentId);
        this.lastName = new SimpleStringProperty(lastName);
        this.firstName = new SimpleStringProperty(firstName);
        this.guardianName = new SimpleStringProperty(guardianName);
        this.guardianEmail = new SimpleStringProperty(guardianEmail);
    }

    public String getStudentId() {
        return studentId.get();
    }

    public void setStudentId(String studentId) {
        this.studentId.set(studentId);
    }

    public StringProperty studentIdProperty() {
        return studentId;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getGuardianName() {
        return guardianName.get();
    }

    public void setGuardianName(String guardianName) {
        this.guardianName.set(guardianName);
    }

    public StringProperty guardianNameProperty() {
        return guardianName;
    }

    public String getGuardianEmail() {
        return guardianEmail.get();
    }

    public void setGuardianEmail(String guardianEmail) {
        this.guardianEmail.set(guardianEmail);
    }

    public StringProperty guardianEmailProperty() {
        return guardianEmail;
    }
}
