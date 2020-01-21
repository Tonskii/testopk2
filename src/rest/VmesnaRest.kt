package rest

import java.io.IOException
import java.text.ParseException
import java.util.ArrayList

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
import iskanje.KnjigomatKnjiga
import projekt.Knjiga
import projekt.KnjigaDao
import projekt.Knjigomat


@Path("/vmesna")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class VmesnaRest {

    @EJB
    private val Knjigomatejb: KnjigomatEJB? = null

    @EJB
    private val narociloEjb: NarociloEJB? = null

    @EJB
    private val ejbKnjiga: KnjigaDao? = null

    @EJB
    private val vmensaEjb: KnjigomatKnjigaEJB? = null

    @Context
    private val context: UriInfo? = null


    @POST
    @Path("/vrniLokacijo/{idKnjiga}")
    @Produces("application/json")
    @Throws(IOException::class, ParseException::class)
    fun vrniLok(@PathParam("idKnjiga") idKnjiga: Int): Response {
        val k = ejbKnjiga!!.najdId(idKnjiga)
        var index = 0
        for (vm in vmensaEjb!!.vrniVse()) {
            if (vm.knjiga!!.id == idKnjiga) {
                index = vm.masina!!.id
            }
        }
        val lokacija = Knjigomatejb!!.najd(index).lokacija
        println("lokacija " + lokacija!!)
        return Response.ok(lokacija).build()


    }


}
