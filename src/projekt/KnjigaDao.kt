package projekt

import java.security.SecureRandom
import java.util.Base64

import javax.ejb.Local
import javax.ejb.Stateless
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Stateless
@Local
class KnjigaDao {


    var knjiga = Knjiga()


    @PersistenceContext(unitName = "Praktikum")
    private val em: EntityManager? = null

    val knjige: List<Knjiga>
        get() = em!!.createQuery("SELECT k FROM Knjiga k").getResultList()

    val knjigeSt: String
        get() {
            val knj = em!!.createQuery("SELECT k FROM Knjiga k").getResultList()
            return knj.size + ""
        }

    fun addBook(slika: ByteArray) {
        val ab = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        val rnd = SecureRandom()
        val sb = StringBuilder(50)
        for (i in 0..49)
            sb.append(ab[rnd.nextInt(ab.length)])
        val qrk = sb.toString()
        knjiga.qrKoda = qrk
        knjiga.slika = slika
        println(slika)
        println("Dodajam $knjiga.")
        em!!.persist(knjiga)
        knjiga = Knjiga()
    }

    fun getKnjigaId(id: Int): List<Knjiga> {
        return em!!.createQuery("select a from Knjiga a where a.id=$id").getResultList()
    }

    fun getKnjigaIde(id: Int): Knjiga {
        return em!!.createQuery("select a from Knjiga a where a.id=$id").getSingleResult()
    }

    fun deleteKnjiga(value: String) {
        println("Bri�em $knjiga.")
    }

    fun najdId(id: Int): Knjiga {

        return em!!.find(Knjiga::class.java, id)
    }

    fun posodobi(k: Knjiga) {
        em!!.merge(k)
    }

    fun brisi(k: Knjiga) {
        em!!.remove(if (em!!.contains(k)) k else em!!.merge(k))

    }


}
