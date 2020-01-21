package EJB

import javax.ejb.Local
import javax.ejb.Stateless
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

import projekt.Knjiga
import projekt.Knjigomat

@Stateless
@Local
class KnjigomatEJB {
    @PersistenceContext
    internal var em: EntityManager? = null

    val knjigomatSt: String
        get() {
            val knj = em!!.createQuery("SELECT k FROM Knjigomat k").getResultList()
            return knj.size + ""
        }

    fun dodajKnjikomat(k: Knjigomat) {
        em!!.persist(k)

    }


    fun getKnjigomat(id: Int): Knjigomat {
        return em!!.createQuery("select a from Knjigomat a where a.idKnjigomat=$id").getSingleResult()

    }


    fun vrniVse(): List<Knjigomat> {
        return em!!.createQuery("select a from Knjigomat a ").getResultList()

    }


    fun najd(id: Int): Knjigomat {

        return em!!.find(Knjigomat::class.java, id)
    }


    fun update(k: Knjigomat) {
        em!!.merge(k)

    }


    fun brisi(k: Knjigomat) {
        em!!.remove(if (em!!.contains(k)) k else em!!.merge(k))

    }


}
