package kc_energy_v2;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Session;
import javax.mail.Transport;

public class SendEmail {
	private String accountEmail;
	private String invoicePath;
	private boolean sendStatus;
	public SendEmail(String accountEmail, String invoicePath) {
		this.accountEmail = accountEmail;
		this.invoicePath = invoicePath;
	}
	
	public boolean getSendStatus() {
		return sendStatus;
	}
	
	public void send() {
		// email ID of Recipient.
		String recipient = accountEmail;

		// email ID of Sender.
		String sender = "KC.Energy@gmail.com";

		// using host as localhost
		String host = "127.0.0.1";

		// Getting system properties
		Properties properties = System.getProperties();
		
		// Setting up mail server
		properties.setProperty("mail.smtp.host", host);

		// creating session object to get properties
		Session session = Session.getDefaultInstance(properties);

		try
		{
			// MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From Field: adding senders email to from field.
			message.setFrom(new InternetAddress(sender));

			// Set To Field: adding recipient's email to from field.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

			// Set Subject: subject of the email
			message.setSubject("KC Energy Invoice");

			// Set body text for email
			BodyPart messageBodyPart = new MimeBodyPart(); 
			messageBodyPart.setText("Thank you for being a loyal KC Energy customer. "
					+ "Please find the invoice attached to this email. Thank you for your business!");
			
			// Set attachment for email
			MimeBodyPart attachmentPart = new MimeBodyPart();
			try {
				attachmentPart.attachFile(new File(invoicePath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// create multipart object to hold the text part and attachment part
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			multipart.addBodyPart(attachmentPart);
			
			// add multipart object to the email
			message.setContent(multipart);
			
			// Send email.
			Transport.send(message);
			sendStatus = true;
		}
		catch (MessagingException mex)
		{
			sendStatus = false;
		}
	}

}
