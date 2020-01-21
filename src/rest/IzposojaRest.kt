package rest

import java.sql.Date
import java.util.ArrayList
import java.util.Calendar

import javax.ejb.EJB
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

import EJB.IzposojaEJB
import EJB.KnjigomatEJB
import EJB.KnjigomatKnjigaEJB
import EJB.NarociloEJB
import EJB.UporabnikEJB
import iskanje.KnjigomatKnjiga
import projekt.Izposoja
import projekt.Knjiga
import projekt.KnjigaDao
import projekt.Knjigomat
import projekt.Mailer
import projekt.Narocilo
import projekt.Uporabnik

@Path("/izposoja")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class IzposojaRest {


    @EJB
    private val ejb: IzposojaEJB? = null

    @EJB
    private val knjEjb: KnjigaDao? = null

    @EJB
    private val upoEjb: UporabnikEJB? = null

    @EJB
    private val narEjb: NarociloEJB? = null

    @EJB
    private val masinaEjb: KnjigomatEJB? = null

    @EJB
    private val vmesniEjb: KnjigomatKnjigaEJB? = null

    @Context
    private val context: UriInfo? = null


    /*Omogocen POST knjiga*/
    @POST
    @Path("/izpo/{narId}&{knjId}&{upoId}")
    fun izposodi(@PathParam("narId") idNar: Int, @PathParam("knjId") idKnj: Int, @PathParam("upoId") idUpo: Int): Response {
        val n = narEjb!!.najd(idNar)
        val k = knjEjb!!.najdId(idKnj)
        val u = upoEjb!!.najdId(idUpo)
        val i = Izposoja()
        val mailer = Mailer()
        val knkn = vmesniEjb!!.getKnjigomatKnjiga(idKnj)
        knkn!!.isJeNoter = false
        knkn.isJeSposojena = true
        vmesniEjb.update(knkn)
        val date = java.sql.Date(Calendar.getInstance().time.time)
        val novi = java.sql.Date(date.time + 14L * 24L * 60L * 60L * 1000L)
        println(novi)
        i.datumDo = novi
        i.datumOd = date
        i.isStanje = true
        i.knjiga = k
        i.upo = u
        val dat = i.datumDo.toString() + " "
        mailer.akcijaIzp(u.email, dat, k.naslov)
        ejb!!.dodajizposoja(i)
        k.stanje = "izposojena"
        knjEjb.posodobi(k)
        n.stanje = false
        narEjb.update(n)

        return Response.ok("uspesno").build()

    }

    //****************************************************** SPREMENI IZ ID V QR
    @POST
    @Path("/vrni/{qr}")
    fun vrniKnjigo(@PathParam("qr") qr: String): Response {

        val vseKnjige = knjEjb!!.knjige
        var izpo = "nedela"
        for (kn in vseKnjige) {

            //izpo+=kn.getQrKoda()+", ";
            if (kn.qrKoda == qr) {            //tukaj spremeni-- vazi bom ;)

                val vseIz = ejb!!.vrniVse()

                for (iz in vseIz) {

                    println("dela" + iz.knjiga.id + kn.id)
                    if (iz.knjiga.id == kn.id && iz.isStanje == true) {
                        izpo = "dela"
                        println("delaas")

                        iz.isStanje = false
                        println(iz.isStanje)
                        ejb.update(iz)
                        kn.stanje = "vrnjena"
                        knjEjb.posodobi(kn)

                        break
                    }
                }
            }
        }


        println(izpo)
        return Response.ok(izpo).build()

    }

    @POST
    @Path("/izpis/{idUpo}")
    fun izpis(@PathParam("idUpo") idUpo: Int): Response {
        val prva = ejb!!.vrniVse()
        val izpo = ArrayList<Izposoja>()
        for (i in prva) {
            if (i.upo.id == idUpo) {
                izpo.add(i)
            }
        }
        println(prva.size)
        println(izpo)
        return Response.ok(izpo).build()

    }

    @POST
    @Path("/izposoja/{knjId}&{upoId}&{masina}")
    fun izposodiBrezNar(@PathParam("knjId") idKnj: Int, @PathParam("upoId") idUpo: Int, @PathParam("masina") idmasin: Int): Response {

        val k = knjEjb!!.najdId(idKnj)
        val u = upoEjb!!.najdId(idUpo)
        val i = Izposoja()
        val mailer = Mailer()
        val date = java.sql.Date(Calendar.getInstance().time.time)
        val novi = java.sql.Date(date.time + 14L * 24L * 60L * 60L * 1000L)
        i.datumDo = novi
        i.datumOd = date
        i.isStanje = true
        i.knjiga = k
        i.upo = u
        val knkn = vmesniEjb!!.getKnjigomatKnjiga(idKnj)
        knkn!!.isJeNoter = false
        knkn.isJeSposojena = true
        vmesniEjb.update(knkn)
        mailer.akcijaIzp(u.email, novi.toString() + "", k.naslov)
        ejb!!.dodajizposoja(i)
        k.stanje = "izposojena"
        knjEjb.posodobi(k)
        val ma = masinaEjb!!.najd(idmasin)
        ma.prostor = ma.prostor + 1


        return Response.ok("uspesno").build()

    }

    @POST
    @Path("/vrniDatum/{knjId}")
    fun vrniDat(@PathParam("knjId") idKnj: Int): Response {
        val vse = ejb!!.vrniVse()
        val datum = ArrayList<String>()
        for (i in vse) {
            if (i.knjiga.id == idKnj) {
                datum.add(i.datumDo.toString() + " ")
            }
        }


        return Response.ok(datum).build()

    }
}
