package rest

import java.io.IOException
import java.text.ParseException
import java.util.ArrayList
import java.util.Calendar

import javax.ejb.EJB
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo


import EJB.KnjigomatEJB
import EJB.KnjigomatKnjigaEJB
import EJB.NarociloEJB
import EJB.UporabnikEJB
import iskanje.IskanjeDela
import iskanje.KnjigomatKnjiga
import projekt.Knjiga
import projekt.KnjigaDao
import projekt.Knjigomat
import projekt.Mailer
import projekt.Narocilo
import projekt.Uporabnik


@Path("/narocilo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class NarociloRest {
    @EJB
    private val ejb: NarociloEJB? = null

    @EJB
    private val knjigaEjb: KnjigaDao? = null

    @EJB
    private val knjigomatEjb: KnjigomatEJB? = null

    @EJB
    private val upoEjb: UporabnikEJB? = null

    @EJB
    private val vmensaEjb: KnjigomatKnjigaEJB? = null

    @Context
    private val context: UriInfo? = null

    /*Dodaj narocilo*/
    @POST
    @Path("/dodaj/{upo}&{knjiga}&{masina}")
    @Produces("application/json")
    @Throws(IOException::class, ParseException::class)
    fun iskanjeKnjige(@PathParam("upo") idUpo: Int, @PathParam("knjiga") idKnjija: Int, @PathParam("masina") masinaLokacija: String): Response {
        val result = ""
        val k = knjigaEjb!!.najdId(idKnjija)

        var idknjigomat = 0
        var imeKnjigomat: String? = ""
        val vsi = knjigomatEjb!!.vrniVse()
        var masina: Knjigomat? = null
        for (knj in vsi) {
            if (knj.lokacija == masinaLokacija) {
                masina = knj
                idknjigomat = knj.id
                imeKnjigomat = knj.ime
            }
        }
        if (masina!!.prostor - 1 < 0) {
            return Response.ok("polno").build()
        } else {
            val knkn = KnjigomatKnjiga(k, masina, true, false)
            val upo = upoEjb!!.najdId(idUpo)
            val nar = Narocilo()
            val mailer = Mailer()
            val date = java.sql.Date(Calendar.getInstance().time.time)
            val novi = java.sql.Date(date.time + 14L * 24L * 60L * 60L * 1000L)
            println("DATUM $novi")
            nar.datumDo = novi
            nar.datumOd = date
            nar.knjiga = k
            nar.knjigomat = masina
            nar.uporabnik = upo
            nar.stanje = true
            mailer.akcijaNar(upo.email, upo.getQrUporabnik(), "${novi} ", k.naslov, imeKnjigomat!!)
            ejb!!.dodajNarocilo(nar)


            k.stanje = "narocena"
            knjigaEjb.posodobi(k)
            masina.prostor = masina.prostor - 1

            vmensaEjb!!.dodajKnjikomatKnjiga(knkn)



            knjigomatEjb.update(masina)


            return Response.ok("Uspesno").build()
        }
    }

    /*Dobi narocila za uporabnika*/
    @POST
    @Path("/vrni/{upo}")
    @Produces("application/json")
    @Throws(IOException::class, ParseException::class)
    fun vrniNarocila(@PathParam("upo") idUpo: Int): Response {
        val vsi = ejb!!.narocila
        val koncna = ArrayList<Int>()
        for (n in vsi) {
            if (n.uporabnik.id == idUpo && n.stanje == true) {
                koncna.add(n.id)
            }
        }

        return if (koncna.size > 0) {
            Response.ok(koncna).build()
        } else
            Response.ok().build()

    }

    @POST
    @Path("/celo/{upo}&{masina}")
    @Produces("application/json")
    @Throws(IOException::class, ParseException::class)
    fun vrniNar(@PathParam("upo") idUpo: Int, @PathParam("masina") masina: Int): Response {
        val vsi = ejb!!.narocila
        val vsi2 = ArrayList<Narocilo>()
        val knjige = ArrayList<Knjiga>()
        for (n in vsi) {
            if (n.stanje == true && n.knjigomat.id == masina && n.uporabnik.id == idUpo) {
                vsi2.add(n)

            }
        }

        for (n in vsi2) {
            val k = knjigaEjb!!.najdId(n.knjiga.id)
            knjige.add(k)

        }


        return if (vsi.size > 0) {
            Response.ok(knjige).build()
        } else
            Response.ok().build()

    }

    @POST
    @Path("/vrniID/{upo}&{knjiga}&{masina}")
    @Produces("application/json")
    @Throws(IOException::class, ParseException::class)
    fun vrniID(@PathParam("upo") idUpo: Int, @PathParam("knjiga") idKnjija: Int, @PathParam("masina") masinaID: Int): Response {

        val vsi = ejb!!.narocila
        var koncna = 0
        for (n in vsi) {
            if (n.knjigomat.id == masinaID && n.uporabnik.id == idUpo && n.knjiga.id == idKnjija) {
                koncna = n.id
            }

        }
        return Response.ok(koncna).build()
    }
}
