package studentrecordsystem;

public class Student {
    private String id;          // Maps to "Student_ID" column
    private String lastName;    // Maps to "Last Name" column
    private String firstName;   // Maps to "First Name" column
    private String parentName;  // Maps to "Parent's Name" column
    private String parentEmail; // Maps to "Parent's Email" column

    // Constructor
    public Student(String id, String lastName, String firstName, String parentName, String parentEmail) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.parentName = parentName;
        this.parentEmail = parentEmail;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", parentName='" + parentName + '\'' +
                ", parentEmail='" + parentEmail + '\'' +
                '}';
    }
}