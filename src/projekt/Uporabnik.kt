package projekt

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "Uporabnik")
class Uporabnik {

    @get:Id
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Column(name = "idUporabnik")
    var id: Int = 0
    var ime: String
    var priimek: String
    var qrUporabnik: String
    var password: String
    var email: String

    fun getQrUporabnik(): String {
        return qrUporabnik
    }

    fun setQrUporabnik(qrUporabnik: String) {
        val qr = ime + priimek + email + password
        var md: MessageDigest? = null
        try {
            md = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        val hashInBytes = md!!.digest(qr.toByteArray(StandardCharsets.UTF_8))

        val sb = StringBuilder()
        for (b in hashInBytes) {
            sb.append(String.format("%02x", b))
        }

        this.qrUporabnik = sb.toString()
    }


}
