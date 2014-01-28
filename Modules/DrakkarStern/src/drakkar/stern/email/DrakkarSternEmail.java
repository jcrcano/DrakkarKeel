/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.email;

import drakkar.oar.util.OutputMonitor;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class DrakkarSternEmail {

    String user;
    String password;
    private String hostSmtp;
    private int portSmtp;
    String email;//senderAddress


    /**
     *
     */
    public DrakkarSternEmail() {
    }
    

    /**
     *
     * @param config
     */
    public DrakkarSternEmail(EmailConfig config) {

        this.user = config.getUser();
        this.password = config.getPassword();
        this.hostSmtp = config.getHostSmtp();
        this.portSmtp = config.getPortSmtp();
        this.email = config.getEmail();

    }



    /**
     * 
     * @param user
     * @param password
     * @param email
     * @param hostSmtp
     * @param portSmtp
     */
    public DrakkarSternEmail(String user, String password, String email, String hostSmtp, int portSmtp) {
        this.user = user;
        this.password = password;
        this.hostSmtp = hostSmtp;
        this.portSmtp = portSmtp;
        this.email = email;
    }

   
    
    
    /**
     *
     * @param toAddress     
     * @param ccAddress
     * @param bccAddress
     * @param subject
     * @param isHTMLFormat
     * @param body
     * @param debug
     * @return
     */
    public boolean send(String toAddress,
            String ccAddress, String bccAddress, String subject,
            boolean isHTMLFormat, StringBuffer body, boolean debug) {

        Session session;
        MimeMultipart multipart = new MimeMultipart();

        Properties properties = new Properties();

        properties.put("mail.smtp.host", hostSmtp);
        //properties.put("mail.smtp.starttls.enable", "false");
        properties.put("mail.smtp.port", portSmtp);
        properties.put("mail.smtp.user", user);
        properties.put("mail.smtp.auth", "true");
        session = Session.getDefaultInstance(properties);
        session.setDebug(debug);
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(email));
            msg.setRecipients(Message.RecipientType.TO, toAddress);
            msg.setRecipients(Message.RecipientType.CC, ccAddress);
            msg.setRecipients(Message.RecipientType.BCC, bccAddress);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // BODY
            MimeBodyPart mbp = new MimeBodyPart();
            if (isHTMLFormat) {
                mbp.setContent(body.toString(), "text/html");
            } else {
                mbp.setText(body.toString());
            }

            multipart.addBodyPart(mbp);

            msg.setContent(multipart);
            // Transport.send(msg);

            Transport t = session.getTransport("smtp");
            t.connect((String) properties.get("mail.smtp.user"), password);
            t.sendMessage(msg, msg.getAllRecipients());
            t.close();
        } catch (Exception mex) {
            OutputMonitor.printStream(" MailSender.send() ", mex);
            return false;
        }
        return true;
    }


    /**
     * 
     * @param toAddress
     * @param subject
     * @param isHTMLFormat
     * @param body
     * @param debug
     * @return
     */
    public boolean send(String toAddress,String subject,
            boolean isHTMLFormat, StringBuffer body, boolean debug) {

        Session session;
        MimeMultipart multipart = new MimeMultipart();
        Properties properties = new Properties();

        properties.put("mail.smtp.host", hostSmtp);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", portSmtp);
        properties.put("mail.smtp.user", user);
        properties.put("mail.smtp.auth", "true");
        session = Session.getDefaultInstance(properties);
        session.setDebug(debug);
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(email));
            msg.setRecipients(Message.RecipientType.TO, toAddress);          
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // BODY
            MimeBodyPart mbp = new MimeBodyPart();
            if (isHTMLFormat) {
                mbp.setContent(body.toString(), "text/html");
            } else {
                mbp.setText(body.toString());
            }

            multipart.addBodyPart(mbp);

            msg.setContent(multipart);
            // Transport.send(msg);

            Transport t = session.getTransport("smtp");
            t.connect((String) properties.get("mail.smtp.user"), password);
            t.sendMessage(msg, msg.getAllRecipients());
            t.close();
        } catch (Exception mex) {
           OutputMonitor.printStream("MailSender.send(). ",mex);
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     */
    public String getHostSmtp() {
        return hostSmtp;
    }

    /**
     *
     * @param hostSmtp
     */
    public void setHostSmtp(String hostSmtp) {
        this.hostSmtp = hostSmtp;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public int getPortSmtp() {
        return portSmtp;
    }

    /**
     * 
     * @param portSmtp
     */
    public void setPortSmtp(int portSmtp) {
        this.portSmtp = portSmtp;
    }

    /**
     *
     * @return
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }



}


