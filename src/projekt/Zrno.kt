package projekt

import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.Serializable
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.text.ParseException


import java.util.ArrayList
import java.util.Base64
import java.util.Collections

import javax.ejb.EJB
import javax.enterprise.context.SessionScoped
import javax.inject.Named
import javax.servlet.http.Part

import org.primefaces.model.DefaultStreamedContent
import org.primefaces.model.StreamedContent

import EJB.IzposojaEJB
import EJB.KnjigomatEJB
import EJB.KnjigomatKnjigaEJB
import EJB.NarociloEJB
import EJB.UporabnikEJB
import iskanje.IskanjeDela
import iskanje.KnjigomatKnjiga

@Named("Zrno")
@SessionScoped
class Zrno : Serializable {
    var uploadedFile: Part? = null

    var knjigaInput: String? = null
    var imenaKnjig: List<String>? = null

    var kn = Knjigomat()
    var up = Uporabnik()

    @EJB
    internal var izposojaEjb: IzposojaEJB? = null

    @EJB
    internal var narociloEjb: NarociloEJB? = null

    @EJB
    var knjigaDao: KnjigaDao

    @EJB
    var upo: UporabnikEJB

    @EJB
    var knjigomat: KnjigomatEJB

    @EJB
    internal var knjiEjb: KnjigomatKnjigaEJB? = null

    var mailer = Mailer()

    var image: StreamedContent? = null
    var cat: String? = null
    var isci: String? = null
    var prikazIndex: List<Int> = ArrayList()
    private var prikaz: MutableList<Knjiga> = ArrayList()

    fun filaj() {
        sprazniKnjigomate()
        val knjigomati = knjigomat.vrniVse()
        for (k in knjigomati) {
            val vrsta = najboljIzposojene(k.id)
            val knjige = knjigaDao.knjige
            Collections.shuffle(knjige)
            val zaNoter = ArrayList<Knjiga>()
            val a = ((k.skupajProstor - 10) * 0.75).toInt()
            println("vrsta" + vrsta + " " + k.id)
            for (kn in knjige) {
                if (kn.stanje != "navoljo") continue
                if (vrsta !== "") {
                    if (kn.vrsta == vrsta) {
                        zaNoter.add(kn)
                    }
                } else {
                    zaNoter.add(kn)
                }

            }
            var j = 0
            for (i in zaNoter.indices) {
                dodajVKnjigomat(zaNoter[i], k)
                j = j + 1
                k.prostor = k.prostor - 1
                knjigomat.update(k)
                if (j >= a) break
            }
            for (kn in knjige) {
                if (kn.stanje == "navoljo") {
                    if (k.prostor <= 10) break
                    dodajVKnjigomat(kn, k)
                    k.prostor = k.prostor - 1
                    knjigomat.update(k)
                }
            }
        }


        //napolni.sprazniKnjigomate();
        //napolni.najboljIzposojene(1);
    }


    fun dodajVKnjigomat(knj: Knjiga, kn: Knjigomat) {
        knj.stanje = "vmasini"
        knjigaDao.posodobi(knj)
        val knjkn = KnjigomatKnjiga(knj, kn, true, false)
        knjiEjb!!.update(knjkn)
    }

    fun zazeni() {

        println("t")

        for (i in 1..Integer.parseInt(knjigaDao.knjigeSt)) {
            knjigaDao.knjiga = knjigaDao.getKnjigaIde(i)
            val path = Paths.get("C:\\slikeKnj", "k$i.jpg")
            var b: ByteArray? = null
            try {

                b = Files.readAllBytes(path)
                println(b!!.size)

            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            knjigaDao.knjiga.slika = b
            knjigaDao.posodobi(knjigaDao.knjiga)

        }
    }


    fun dodajKnjigo() {
        var fileName = ""
        for (cd in uploadedFile!!.getHeader("content-disposition").split(";")) {
            if (cd.trim({ it <= ' ' }).startsWith("filename")) {
                fileName = cd.substring(cd.indexOf('=') + 1).trim({ it <= ' ' })
                fileName = fileName.substring(1, fileName.length - 1)
            }

        }
        println(fileName)
        //Path patha=Paths.get(fileName);
        val path = saveFile()
        var b: ByteArray? = null
        try {

            b = Files.readAllBytes(path!!)
            println(b!!.size)
            Files.delete(path)
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }


        knjigaDao.addBook(b)
    }

    fun izbrisiKnjigo(id: Int) {

        var knji: KnjigomatKnjiga
        val knkn = knjiEjb!!.vrniVse()
        for (kn in knkn) {
            if (kn.knjiga!!.id == id) {
                knji = kn
                knjiEjb!!.brisi(knji)
            }
        }

        var nar: Narocilo
        val naro = narociloEjb!!.narocila
        for (kn in naro) {
            if (kn.knjiga.id == id) {
                nar = kn
                narociloEjb!!.brisi(nar)
            }
        }

        var izp: Izposoja
        val izpo = izposojaEjb!!.vrniVse()
        for (kn in izpo) {
            if (kn.knjiga.id == id) {
                izp = kn
                izposojaEjb!!.brisi(izp)
            }
        }

        val brisi = knjigaDao.najdId(id)
        knjigaDao.brisi(brisi)
    }

    fun spremeniKnjigo(id: Int) {
        val posodobi = knjigaDao.najdId(id)
        knjigaDao.posodobi(posodobi)
    }

    /*Iskanje*/
    @Throws(IOException::class, ParseException::class)
    fun isciBaza() {
        prikaz = ArrayList()
        ArrayList<Int>()
        println("Not")
        prikazIndex = IskanjeDela.isci(knjigaDao.knjige, isci, cat)
        for (i in prikazIndex) {
            println("IDJI: $i")
            val vmesnaKnj = knjigaDao.getKnjigaId(i)[0]
            prikaz.add(vmesnaKnj)
        }
    }

    fun sprazniKnjigomate() {
        val knkn = knjiEjb!!.vrniVse()
        for (kn in knkn) {
            if (kn.isJeNoter == true)
                kn.isJeNoter = false
            knjiEjb!!.update(kn)
        }
        val knjige = knjigaDao.knjige
        for (k in knjige) {
            if (k.stanje == "vmasini" || k.stanje == "vrnjena" || k.stanje == "naroceno") {
                k.stanje = "navoljo"
                knjigaDao.posodobi(k)
            }
        }
        val knjigomati = knjigomat.vrniVse()
        for (k in knjigomati) {
            k.prostor = k.skupajProstor
            knjigomat.update(k)
        }
    }


    fun najboljIzposojene(idKnjigomat: Int): String {
        val vrste = ArrayList<String>()
        val stevilo = ArrayList<Int>()

        val knkn = knjiEjb!!.vrniVse()
        if (knkn != null || knkn!!.size > 0) {
            var nas = false
            for (i in knkn!!.indices) {
                if (knkn[i].masina!!.id != idKnjigomat)
                    continue
                if (knkn[i].isJeSposojena == false)
                    continue
                print(i)
                nas = false
                for (j in vrste.indices) {
                    if (knkn[i].knjiga!!.vrsta == vrste[j]) {
                        stevilo[j] = stevilo[j] + 1
                        nas = true
                        break
                    }


                }
                if (nas == true) continue
                vrste.add(knkn[i].knjiga!!.vrsta)
                stevilo.add(1)
            }
            if (vrste.size > 0) {
                var max = 0
                var index = 0
                for (j in stevilo.indices) {
                    if (stevilo[j] > max) {
                        max = stevilo[j]
                        index = j
                    }
                }
                println(knkn)
                println(vrste)
                println(stevilo)

                return vrste[index]
            } else
                return ""
        } else
            return ""
    }

    /*Dodajnaje knjigomatov*/
    fun dodajKnjigomat() {
        knjigomat.dodajKnjikomat(kn)
        kn = Knjigomat()
    }

    /*Dodajnaje uporabnika*/
    fun dodajUporabnika() {
        up.setQrUporabnik("")
        upo.dodajUporabnika(up)
        up = Uporabnik()
    }

    /*Brisanje knjigomatov*/
    fun izbrisiKnjigomat(id: Int) {
        var knji: KnjigomatKnjiga
        val knkn = knjiEjb!!.vrniVse()
        for (kn in knkn) {
            if (kn.masina!!.id == id) {
                knji = kn
                knjiEjb!!.brisi(knji)
            }
        }

        var nar: Narocilo
        val naro = narociloEjb!!.narocila
        for (kn in naro) {
            if (kn.knjigomat.id == id) {
                nar = kn
                narociloEjb!!.brisi(nar)
            }
        }

        val brisi = knjigomat.najd(id)
        knjigomat.brisi(brisi)
    }

    fun izbrisiUporabnika(id: Int) {

        var nar: Narocilo
        val naro = narociloEjb!!.narocila
        for (kn in naro) {
            if (kn.uporabnik.id == id) {
                nar = kn
                narociloEjb!!.brisi(nar)
            }
        }

        var izp: Izposoja
        val izpo = izposojaEjb!!.vrniVse()
        for (kn in izpo) {
            if (kn.upo.id == id) {
                izp = kn
                izposojaEjb!!.brisi(izp)
            }
        }
        val brisi = upo.najdId(id)
        upo.brisi(brisi)
    }


    fun saveFile(): Path? {
        var path: Path? = null
        var fileName = ""
        for (cd in uploadedFile!!.getHeader("content-disposition").split(";")) {
            if (cd.trim({ it <= ' ' }).startsWith("filename")) {
                fileName = cd.substring(cd.indexOf('=') + 1).trim({ it <= ' ' })
            }

        }
        println(fileName)
        if (fileName.indexOf("\\") != -1) {
            fileName = fileName.substring(fileName.lastIndexOf("\\"))
        }
        print(fileName)
        fileName = fileName.substring(1, fileName.length - 1)
        try {
            uploadedFile!!.getInputStream().use({ input ->


                println("dela tu")
                Files.copy(input, File("C:\\Slike", fileName).toPath())
                path = Paths.get("C:\\Slike", fileName)

            })
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return path
    }

    fun getPrikaz(): List<Knjiga> {
        return prikaz
    }

    fun setPrikaz(prikaz: MutableList<Knjiga>) {
        this.prikaz = prikaz
    }

    companion object {

        /**
         *
         */
        private const val serialVersionUID = -3108130695607179483L
    }


}
