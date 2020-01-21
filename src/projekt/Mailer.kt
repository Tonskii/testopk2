package projekt


import javax.annotation.Resource
import javax.ejb.Stateless
import javax.mail.BodyPart
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.PasswordAuthentication
import java.util.Properties


import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.sun.mail.handlers.text_plain

class Mailer {

    fun akcijaIzp(email: String, datum: String, naslov: String) {
        //tip   1-narocilo ,  2-izposoj
        //String email="ziga.susin@gmail.com";
        val from = "knjigomatMaribor"
        val host = "smtp.gmail.com"//or IP address
        val log = LoggerFactory.getLogger(Zrno::class.java!!)
        log.info(email + "prvi del")
        //Get the session object
        val properties = System.getProperties()
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true"
        properties.setProperty("mail.smtp.host", host)
        properties["mail.smtp.port"] = "587"
        val session = Session.getInstance(properties,
                object : javax.mail.Authenticator() {
                    protected val passwordAuthentication: PasswordAuthentication
                        get() = PasswordAuthentication(from, "praktikum123")
                })

        //compose the message
        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(from))
            message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
            message.setSubject("Izposojena knjiga")
            message.setText("Izposojena knjiga: $naslov\nDatum vracila: $datum")


            // Send message
            Transport.send(message)
            println("message sent successfully....")

            log.info("$email poslano")
        } catch (mex: MessagingException) {
            mex.printStackTrace()
        }


    }


    fun akcijaNar(email: String, qr: String, datum: String, naslov: String, knjigomat: String) {
        //tip   1-narocilo ,  2-izposoj
        //String email="ziga.susin@gmail.com";
        val from = "knjigomatMaribor"
        val host = "smtp.gmail.com"//or IP address
        val log = LoggerFactory.getLogger(Zrno::class.java!!)
        log.info(email + "prvi del")
        //Get the session object
        val properties = System.getProperties()
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true"
        properties.setProperty("mail.smtp.host", host)
        properties["mail.smtp.port"] = "587"
        val session = Session.getInstance(properties,
                object : javax.mail.Authenticator() {
                    protected val passwordAuthentication: PasswordAuthentication
                        get() = PasswordAuthentication(from, "praktikum123")
                })

        //compose the message
        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(from))
            message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
            message.setSubject("Narocena knjiga")
            message.setText("Izposojena knjiga: $naslov\nKnjigo prevzamite do: $datum\n")


            //HTML
            val mp = MimeMultipart()
            val mbp = MimeBodyPart()
            val bsd = MimeBodyPart()

            val besediloa = "Izposojena knjiga: " + naslov + "<br/ >Knjigo prevzamite do: " + datum + " na knjigomatu: " + knjigomat.toUpperCase() + "<br/><br/>"
            val img = "<font style='font-size:15px'>$besediloa</font><br/ ><b style='font-size:20px'>Prevzem mozen na knjigomatu s spodnjo qr kodo:</b><br/><img src=\"https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl=$qr&choe=UTF-8\" />"
            mbp.setContent(img, "text/html")

            //String besedilo = "Izposojena knjiga: "+naslov+"\n"+"Knjigo prevzamite do: " +datum+" na knjigomatu: "+knjigomat.toUpperCase()+"\n";
            //bsd.setContent(besedilo, "text/plain");
            //mp.addBodyPart(bsd);
            mp.addBodyPart(mbp)
            message.setContent(mp)


            // Send message
            Transport.send(message)
            println("message sent successfully....")

            log.info("$email poslano")
        } catch (mex: MessagingException) {
            mex.printStackTrace()
        }


    }

    fun akcijaSpam(email: String, s: String) {
        //tip   1-narocilo ,  2-izposoj
        //String email="ziga.susin@gmail.com";
        val from = "knjigomatMaribor"
        val host = "smtp.gmail.com"//or IP address
        for (i in 0 until s.length) {
            val log = LoggerFactory.getLogger(Zrno::class.java!!)
            log.info(email + "prvi del")
            //Get the session object
            val properties = System.getProperties()
            properties["mail.smtp.auth"] = "true"
            properties["mail.smtp.starttls.enable"] = "true"
            properties.setProperty("mail.smtp.host", host)
            properties["mail.smtp.port"] = "587"
            val session = Session.getInstance(properties,
                    object : javax.mail.Authenticator() {
                        protected val passwordAuthentication: PasswordAuthentication
                            get() = PasswordAuthentication(from, "praktikum123")
                    })

            //compose the message
            try {
                val message = MimeMessage(session)
                message.setFrom(InternetAddress(from))
                message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
                message.setSubject("Narocena knjiga")
                message.setText(s[i] + "")


                // Send message
                Transport.send(message)
                println("message sent successfully....")

                log.info("$email poslano")
            } catch (mex: MessagingException) {
                mex.printStackTrace()
            }

        }

    }


}
