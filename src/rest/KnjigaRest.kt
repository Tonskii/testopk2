package rest

import java.io.IOException
import java.text.ParseException
import java.util.ArrayList
import java.util.Base64

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
import javax.ws.rs.core.SecurityContext
import javax.ws.rs.core.UriInfo

import iskanje.IskanjeDela
import projekt.Knjiga
import projekt.KnjigaDao

@Path("/knjige")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class KnjigaRest {


    @EJB
    private val ejb: KnjigaDao? = null

    @Context
    private val context: UriInfo? = null

    @GET
    fun vrniVseOsebe(): Response {
        return Response.ok(ejb!!.knjige).build()
    }

    /*Omogocen GET knjiga*/
    /*@GET
		@Path("/knjiga/{id}")
		public Response vrniKnjigo(@PathParam("id") String idS) {
			int id = Integer.parseInt(idS);
			Knjiga kn = ejb.najdId(id);
			if (kn != null) {
				return Response.ok(kn).build();
			} else {
				return Response.status(403).entity("KnjigeNiMogoceNajtiException").build();
			}
		}*/

    /*Omogocen POST knjiga*/

    @GET
    @Path("/knjiga/{byte}")
    fun vrniStr(@PathParam("byte") b: ByteArray): String {
        val str = Base64.getEncoder().encodeToString(b)
        println("dela")
        return str
        //return Response.ok(str).build();
    }

    @GET
    @Path("/knjiga/")
    fun vrniStr(): String {

        println("dela")
        return "ja"
        //return Response.ok(str).build();
    }


    @POST
    @Path("/knjiga/{id}")
    fun vrniKnjigoPost(@PathParam("id") idS: String): Response {
        val id = Integer.parseInt(idS)
        val kn = ejb!!.najdId(id)
        return if (kn != null) {
            Response.ok(kn).build()
        } else {
            Response.status(403).entity("KnjigeNiMogoceNajtiException").build()
        }
    }

    /* POST knjiga iskanje*/
    @GET
    @Path("/iskanje/{cat}&{iskanje}")
    @Produces("application/json")
    @Throws(IOException::class, ParseException::class)
    fun iskanjeKnjige(@PathParam("cat") cat: String, @PathParam("iskanje") isci: String): Response {
        val najdene = ArrayList<Knjiga>()

        println(isci)
        val najdeniID = IskanjeDela.isci(ejb!!.knjige, isci, cat)

        for (i in najdeniID) {
            val k = ejb.najdId(i!!)
            najdene.add(k)

        }

        /*for(int i=0;i<najdene.size();i++) {
				najdene.get(i).setSlika(null);
			}*/

        return if (najdene.size > 0) {
            Response.ok(najdene).build()
        } else {
            Response.status(403).entity("KnjigeNiMogoceNajtiException").build()
        }
    }


    @GET
    @Path("/iskanje/")
    @Produces("application/json")
    @Throws(IOException::class, ParseException::class)
    fun iskanjeKnjige(@Context sc: SecurityContext): Response {

        val najdene = ejb!!.knjige

        /*for(int i=0;i<najdene.size();i++) {
				najdene.get(i).setSlika(null);
			}*/

        return if (najdene.size > 0) {
            Response.ok(najdene).build()
        } else {
            Response.status(403).entity("KnjigeNiMogoceNajtiException").build()
        }
    }

}

