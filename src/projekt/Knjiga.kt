package projekt

import java.awt.Graphics2D

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.io.Serializable
import java.io.UnsupportedEncodingException
import java.sql.Date
import java.util.Arrays
import java.util.Base64

import javax.ejb.Singleton
import javax.imageio.ImageIO
import javax.imageio.ImageReadParam
import javax.imageio.ImageReader
import javax.imageio.stream.ImageInputStream
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table
import javax.xml.bind.DatatypeConverter

@Entity
@Table(name = "Knjiga")
@Singleton
class Knjiga @JvmOverloads constructor(var naslov: String? = "", var avtor: String? = "", var vrsta: String? = "", @get:Lob
var slika: ByteArray? = null, var qrKoda: String? = "") : Serializable {

    @get:Id
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Column(name = "idKnjiga")
    var id: Int = 0
    var stanje: String? = null

    init {
        this.stanje = "navoljo"

    }

    fun stringSlika(): String {


        val b = slika
        var string = "a"
        println("slika")

        if (b == null) return "ni slike"
        string = Base64.getEncoder().encodeToString(b)
        println(string)
        return string
    }

    override fun toString(): String {
        return "Knjiga [naslov=" + naslov + ", avtor=" + avtor + ", vrsta=" + vrsta + ", blob=" + slika!!.size + "]"
    }

    companion object {

        /**
         *
         */
        private const val serialVersionUID = 5204134699484276684L
    }


}
