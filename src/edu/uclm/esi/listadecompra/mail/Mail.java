package edu.uclm.esi.listadecompra.mail;

import java.io.PrintStream;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {
    private static String userName = "practica.listacompra@gmail.com";
    private static String password = "listacompra";

    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getInstance((Properties)props, (Authenticator)new Authenticator(){

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
        return session;
    }

    public static void enviarMail(String destino, String asunto, String texto) throws Exception {
        try {
            MimeMessage message = new MimeMessage(Mail.getSession());
            message.setFrom((Address)new InternetAddress(userName));
            message.setRecipients(Message.RecipientType.TO, (Address[])InternetAddress.parse((String)destino));
            message.setSubject(asunto);
            message.setText(texto);
            Transport.send((Message)message);
            System.out.println("Se ha enviado el mail");
        }
        catch (MessagingException e) {
            throw new RuntimeException((Throwable)e);
        }
    }

}