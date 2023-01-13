import java.io.*;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class SendMail {
    public static Properties propFile = new Properties();
    public static FileInputStream fis;

    public static void main(String[] args) {
        String CLIENT_LIST = System.getProperty("user.dir") + "/mail_list.csv";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        try {
            fis = new FileInputStream(System.getProperty("user.dir") + "/Data.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            propFile.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String EMAIL = propFile.getProperty("EMAIL");
        String PASSWORD = propFile.getProperty("PASSWORD");
        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        // Used to debug SMTP issues
        session.setDebug(true);
        try (
                BufferedReader br = new BufferedReader(new FileReader(CLIENT_LIST));
                CSVParser parser = CSVFormat.DEFAULT.withDelimiter(',').withHeader().parse(br)
        ) {
            for (CSVRecord record : parser) {
                String EmailId_CSV = record.get("Email ID");
                String FirstName_CSV = record.get("First Name");
                String LastName_CSV = record.get("Last Name");

                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);
                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(EmailId_CSV));
                // Set Subject: header field
                message.setSubject(String.format("Let's Talk  %s", FirstName_CSV));
                // Now set the actual message (BODY)
                message.setContent(
                        "<div>\n" +
                                "  <div style=\"margin-bottom: 16px;\">Hi " + FirstName_CSV + ",</div>\n" +
                                "  <div style=\"margin-bottom: 16px;\">I'll keep this short and sweet to make the 26 seconds it takes to read this worth your time (yes, I timed it.)</div>\n" +
                                "  <div style=\"margin-bottom: 16px;\">As the Sales Coordinator at Navyug Infosolutions, I get to speak with people like you about their software needs are on my radar because we've helped a lot of companies in your domain.</div>\n" +
                                "  <div style=\"margin-bottom: 16px; font-weight: bold;\">Services we offer</div>\n" +
                                "  <div style=\"margin-bottom: 16px;\">*Web and Mobile Applications </div>\n" +
                                "  <div style=\"margin-bottom: 16px;\">\"Internet of Things Solutions (IoT)\" </div>\n" +
                                "  <div style=\"margin-bottom: 16px;\">*Manual and Automation Testing and Dev Ops Services </div>\n" +
                                "  <div style=\"margin-bottom: 16px;\">*AI, ML, and other custom software development services </div>\n" +
                                "  <div style=\"margin-bottom: 16px;\">Could we schedule a 15- to 20-minute call to discuss your strategy for what excites you, which challenges you to see, and how you envision your plan changing down the road? </div>\n" +
                                "  <div style=\"margin-bottom: 64px;\"> I would love to learn more about your unique situation and see if Navyug could be a good fit for you. If you’re open to making introductions, please reply to this email with your availability. </div>\n" +
                                "  <div style=\"font-weight: bold;\">Thanks & Regards</div>\n" +
                                "  <div style=\"font-weight: bold;\">Richa Khanna</div>\n" +
                                "  <div>Sales Coordinator</div>\n" +
                                "  <div>Mob: 85879 64815</div>\n" +
                                "  <div>Navyug Infosolutions Pvt. Ltd</div>\n" +
                                "</div>",
                        "text/html");

                System.out.println("sending...");
                // Send message
                Transport.send(message);
                System.out.println("Sent message successfully....");

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}