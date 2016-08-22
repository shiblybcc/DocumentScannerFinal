package de.rwthaachen.cbmb.Utility;


import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class Mailer {

    public void sendMail(String emailTo, String subject) throws EmailException{
        String hostName = "smtp.googlemail.com";
        Integer smtpPort = 465;
        String emailFrom = "asdf@gmail.com";
        String emailUsername = "username";
        String emailPassword = "password";


        HtmlEmail email = new HtmlEmail();
//        Configuration
        email.setHostName(hostName);
        email.setSmtpPort(smtpPort);
        email.setAuthenticator(new DefaultAuthenticator(emailUsername, emailPassword));

//        Email receiver details
        email.setFrom(emailFrom);
        email.addTo(emailTo);
        email.setSubject(subject);

//        Email content

//        set normal email
//        email.setMsg("This is a test mail ... :-)");

        // set the html message
        email.setHtmlMsg("<html>" +
                "testing email service here </html>");

        // set the alternative message
        email.setTextMsg("Your email client does not support HTML messages");

        // send the email
        email.send();



    }
}
