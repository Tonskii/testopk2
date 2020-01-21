package EJB

import javax.ejb.Local
import javax.ejb.Stateless
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

import iskanje.KnjigomatKnjiga
import projekt.Knjiga
import projekt.Knjigomat

@Stateless
@Local
class KnjigomatKnjigaEJB {
    @PersistenceContext
    internal var em: EntityManager? = null

    fun dodajKnjikomatKnjiga(k: KnjigomatKnjiga) {
        em!!.persist(k)

    }


    fun getKnjigomatKnjiga(idK: Int): KnjigomatKnjiga? {
        val knkn = em!!.createQuery("select a from KnjigomatKnjiga a").getResultList()
        var k: KnjigomatKnjiga? = null
        for (kn in knkn) {
            if (kn.isJeNoter == true && kn.knjiga!!.id == idK) {
                k = kn
            }
        }
        return k
    }

    fun getKnjigomat(id: Int): List<KnjigomatKnjiga> {
        return em!!.createQuery("select a from Knjigomat a where a.id=$id").getResultList()

    }


    fun vrniVse(): List<KnjigomatKnjiga> {

        return em!!.createQuery("select k from KnjigomatKnjiga k").getResultList()
    }


    fun update(k: KnjigomatKnjiga) {
        em!!.merge(k)

    }

    fun brisi(k: KnjigomatKnjiga) {
        em!!.remove(if (em!!.contains(k)) k else em!!.merge(k))

    }


}


	
