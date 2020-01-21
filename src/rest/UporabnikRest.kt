package rest

import java.util.ArrayList

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

import EJB.UporabnikEJB
import projekt.Uporabnik

@Path("/upoti")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class UporabnikRest {
    @EJB
    private val ejb: UporabnikEJB? = null

    @Context
    private val context: UriInfo? = null


    /*Omogocen POST uporabnik*/
    @POST
    @Path("/upo/{id}")
    fun vrniKnjigoPost(@PathParam("id") idS: String): Response {
        val id = Integer.parseInt(idS)
        val upo = ejb!!.najdId(id)

        return Response.ok(upo).build()

    }


    /*Omogocen POST uporabnik*/
    @POST
    @Path("/upot/")
    fun prijava(): Response {
        //	int id = Integer.parseInt(idS);

        val id = 0
        val vsi = ejb!!.vrniVse()


        return Response.ok(vsi).build()

    }
}