package studentrecordsystem;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    public static boolean sendEmail(String toEmail, String subject, String body) {
        final String fromEmail = "studentrecordsystem103@gmail.com"; // your Gmail
        final String password = "cxdd foyq ufcv pfrb"; // App Password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Fix SSL trust issue sometimes

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            System.out.println("Preparing to send email to: " + toEmail);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully to: " + toEmail);
            return true;

        } catch (AddressException ae) {
            System.err.println("Invalid email address: " + toEmail);
            ae.printStackTrace();
        } catch (MessagingException me) {
            System.err.println("MessagingException: " + me.getMessage());
            me.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error during email sending:");
            e.printStackTrace();
        }

        return false;
    }

    static void send(String guardianEmail, String student_Arrival_Notification, String message) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
