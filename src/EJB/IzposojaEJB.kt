package EJB

import javax.ejb.Local
import javax.ejb.Stateless
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

import projekt.Izposoja
import projekt.Knjiga
import projekt.Knjigomat

@Local
@Stateless
class IzposojaEJB {
    @PersistenceContext
    internal var em: EntityManager? = null

    fun dodajizposoja(i: Izposoja) {
        em!!.persist(i)

    }

    fun najd(id: Int): Izposoja {

        return em!!.find(Izposoja::class.java, id)
    }

    fun vrniVse(): List<Izposoja> {
        return em!!.createQuery("select a from Izposoja a ").getResultList()

    }

    fun update(i: Izposoja) {
        em!!.merge(i)

    }

    fun brisi(k: Izposoja) {
        em!!.remove(if (em!!.contains(k)) k else em!!.merge(k))

    }
}
