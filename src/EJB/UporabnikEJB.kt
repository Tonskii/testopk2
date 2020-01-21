package EJB

import javax.ejb.Local
import javax.ejb.Stateless
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

import projekt.Knjiga
import projekt.Knjigomat
import projekt.Narocilo
import projekt.Uporabnik

@Stateless
@Local
class UporabnikEJB {


    @PersistenceContext(unitName = "Praktikum")
    private val em: EntityManager? = null

    val uporabnikiSt: String
        get() {
            val upo = em!!.createQuery("SELECT u FROM Uporabnik u").getResultList()
            return upo.size + ""
        }


    fun dodajUporabnika(u: Uporabnik) {
        em!!.persist(u)

    }


    fun najdId(id: Int): Uporabnik {

        return em!!.find(Uporabnik::class.java, id)
    }


    fun vrniVse(): List<Uporabnik> {
        return em!!.createQuery("select a from Uporabnik a ").getResultList()

    }

    fun najdi(qr: String): Uporabnik {

        return em!!.createQuery("select a from Uporabnik a where qrUporabnik=$qr").getSingleResult()
    }

    fun brisi(k: Uporabnik) {
        em!!.remove(if (em!!.contains(k)) k else em!!.merge(k))

    }

}
