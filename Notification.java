package studentrecordsystem;

import javafx.beans.property.*;

public class Notification {
    private final IntegerProperty logId;
    private final StringProperty studentId;
    private final StringProperty sentAt;
    private final StringProperty date;
    private final StringProperty time;
    private final StringProperty message;

    public Notification(int logId, String studentId, String sentAt, String date, String time) {
        this.logId = new SimpleIntegerProperty(logId);
        this.studentId = new SimpleStringProperty(studentId);
        this.sentAt = new SimpleStringProperty(sentAt);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.message = new SimpleStringProperty(""); // initially blank
    }

    Notification(int aInt, String string, String string0, String string1, String string2, String string3) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public IntegerProperty logIdProperty() { return logId; }
    public StringProperty studentIdProperty() { return studentId; }
    public StringProperty sentAtProperty() { return sentAt; }
    public StringProperty dateProperty() { return date; }
    public StringProperty timeProperty() { return time; }
    public StringProperty messageProperty() { return message; }

    public void setMessage(String message) {
        this.message.set(message);
    }
}
