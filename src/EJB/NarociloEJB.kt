package EJB

import javax.ejb.Local
import javax.ejb.Stateless
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

import projekt.Knjiga
import projekt.Knjigomat
import projekt.Narocilo

@Stateless
@Local
class NarociloEJB {
    @PersistenceContext
    internal var em: EntityManager? = null

    val narocila: List<Narocilo>
        get() = em!!.createQuery("select a from Narocilo a ").getResultList()


    fun dodajNarocilo(n: Narocilo) {
        em!!.persist(n)

    }

    fun update(n: Narocilo) {
        em!!.merge(n)

    }

    fun najd(id: Int): Narocilo {

        return em!!.find(Narocilo::class.java, id)
    }

    fun brisi(k: Narocilo) {
        em!!.remove(if (em!!.contains(k)) k else em!!.merge(k))

    }
}
