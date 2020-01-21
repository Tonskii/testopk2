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
import projekt.Knjigomat


@Path("/masina")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class KnjigomatRest {

    @EJB
    private val ejb: KnjigomatEJB? = null

    @EJB
    private val narociloEjb: NarociloEJB? = null

    @EJB
    private val vmensaEjb: KnjigomatKnjigaEJB? = null

    @Context
    private val context: UriInfo? = null

    @GET
    fun vrniVseKnjigomate(): Response {
        return Response.ok(ejb!!.vrniVse()).build()
    }


    @POST
    @Path("/vsi/")
    @Produces("application/json")
    @Throws(IOException::class, ParseException::class)
    fun iskanjeKnjige(): Response {
        val najdene = ejb!!.vrniVse()
        val vrni = ArrayList<String>()
        for (k in najdene) {
            vrni.add(k.lokacija)
        }



        return if (najdene.size > 0) {
            Response.ok(vrni).build()
        } else {
            Response.status(403).entity("KnjigeNiMogoceNajtiException").build()
        }
    }

    @POST
    @Path("/iskanjeMasina/{masina}")
    @Produces("application/json")
    @Throws(IOException::class, ParseException::class)
    fun iskanjeMasina(@PathParam("masina") masina: Int): Response {
        val zac = vmensaEjb!!.vrniVse()
        val koncna = ArrayList<Knjiga>()
        for (k in zac) {
            if (k.masina!!.id == masina) {
                if (k.isJeNoter == true) {
                    koncna.add(k.knjiga)
                }
            }
        }
        return if (koncna.size > 0) {
            Response.ok(koncna).build()
        } else {
            Response.status(403).entity("Napaka").build()
        }

        /*Knjigomat najdene = ejb.najd(Integer.parseInt(masina));



		List<KnjigomatKnjiga> prva = vmensaEjb.vrniVse();
		List<Knjiga>vmesna= new ArrayList<Knjiga>();
		for (KnjigomatKnjiga v:prva) {
			if(v.getMasina().getIme().equals(masina)) {
				vmesna.add(v.getKnjiga());
			}
		}


		List<Knjiga> konec=new ArrayList<Knjiga>();
		for(Knjiga k:vmesna) {
			if(k.getStanje().equals("navoljo")) {
				konec.add(k);
			}
		}
		if (najdene!=null) {
			return Response.ok(konec).build();
		} else {
			return Response.status(403).entity("Napaka").build();
		}*/
    }

    @GET
    @Path("/knjigomat/{id}")
    fun vrniKnjigoPost(@PathParam("id") idS: String): Response {
        val id = Integer.parseInt(idS)
        val kn = ejb!!.najd(id)
        return if (kn != null) {
            Response.ok(kn).build()
        } else {
            Response.status(403).entity("KnjigomataNiMogoceNajtiException").build()
        }
    }

}
