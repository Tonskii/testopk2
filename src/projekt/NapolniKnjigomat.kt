package projekt

import java.io.Serializable
import java.util.ArrayList

import javax.ejb.EJB


import EJB.KnjigomatEJB
import EJB.KnjigomatKnjigaEJB
import iskanje.KnjigomatKnjiga


class NapolniKnjigomat : Serializable {

    @EJB
    internal var knknEjb: KnjigomatKnjigaEJB? = null

    @EJB
    internal var knjigaEjb: KnjigaDao? = null

    @EJB
    internal var knjigomatEjb: KnjigomatEJB? = null


    fun sprazniKnjigomate() {
        val knkn = knknEjb!!.vrniVse()
        for (kn in knkn) {
            if (kn.isJeNoter == true)
                kn.isJeNoter = false
            knknEjb!!.update(kn)
        }
        val knjige = knjigaEjb!!.knjige
        for (k in knjige) {
            if (k.stanje == "vmasini" || k.stanje == "vrnjena" || k.stanje == "naroceno") {
                k.stanje = "navoljo"
                knjigaEjb!!.posodobi(k)
            }
        }
        val knjigomati = knjigomatEjb!!.vrniVse()
        for (k in knjigomati) {
            k.prostor = k.skupajProstor
            knjigomatEjb!!.update(k)
        }
    }


    fun najboljIzposojene(idKnjigomat: Int): String {
        val vrste = ArrayList<String>()
        val stevilo = ArrayList<Int>()

        val knkn = knknEjb!!.vrniVse()
        var nas = false
        for (i in knkn.indices) {
            if (knkn[i].id != idKnjigomat)
                continue
            if (knkn[i].isJeSposojena == false)
                continue
            nas = false
            for (j in vrste.indices) {
                if (knkn[i].knjiga!!.vrsta == vrste[j]) {
                    stevilo[j] = stevilo[j] + 1
                    nas = true
                }
                if (nas == true) continue
            }
            vrste.add(knkn[i].knjiga!!.vrsta)
            stevilo.add(1)
        }
        var max = 0
        var index = 0
        for (j in stevilo.indices) {
            if (stevilo[j] > max) {
                max = stevilo[j]
                index = j
            }
        }
        println(vrste)
        println(stevilo)
        return vrste[index]
    }

    fun neki() {
        var knkn: List<KnjigomatKnjiga>? = null
        try {
            knkn = knknEjb!!.vrniVse()
        } catch (e: Exception) {
            // TODO: handle exception
        }

        println(knkn)
    }

    companion object {


        /**
         *
         */
        private const val serialVersionUID = 1L
    }


}
