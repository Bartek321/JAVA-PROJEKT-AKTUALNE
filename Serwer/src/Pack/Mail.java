package Pack;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

public class Mail {
	static Logger logger = Logger.getLogger(Mail.class);
	public static void sendEmail(Session session, String toEmail, String subject, String body){
		try
	    {
	      MimeMessage mimeMessage = new MimeMessage(session);

	      mimeMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
	      mimeMessage.addHeader("format", "flowed");
	      mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");

	      mimeMessage.setFrom(new InternetAddress("powiadom123@gmail.com", "Powiadomienia"));

	      mimeMessage.setReplyTo(InternetAddress.parse("powiadom123@gmail.com", false));

	      mimeMessage.setSubject(subject, "UTF-8");

	      mimeMessage.setText(body, "UTF-8");

	      mimeMessage.setSentDate(new Date());

	      mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false)); 
    	  Transport.send(mimeMessage, "powiadom123@gmail.com", "aaaaa_111"); 
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	}

	public void send(String adress, String body) {    
		
		final String fromEmail = "powiadom123@gmail.com"; //requires valid gmail id
		final String password = "aaaaa_111"; // correct password for gmail id
		final String toEmail = adress; // can be any email id 
		
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
		properties.put("mail.smtp.port", "587"); //TLS Port
		properties.put("mail.smtp.authenticator", "true"); //enable authentication
		properties.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
	
		Authenticator authenticator = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(properties, authenticator);
		if(body.isEmpty())
			logger.warn("empty body");
		sendEmail(session, toEmail,"Powiadomienie - zanieczyszczenie powietrza", body);
	}
}

