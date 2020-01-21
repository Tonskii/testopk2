package iskanje

import java.io.IOException
import java.text.ParseException
import java.util.ArrayList

import javax.ejb.EJB

import EJB.UporabnikEJB
import projekt.Knjiga
import projekt.Uporabnik

object IskanjeDela {

    @Throws(IOException::class, ParseException::class)
    fun isci(knjige: List<Knjiga>, iscem: String, cat: String): List<Int> {
        val koncna = ArrayList<Int>()

        for (k in knjige) {
            if (cat == "naslov") {
                if (k.naslov!!.toLowerCase().contains(iscem.toLowerCase())) {
                    koncna.add(k.id)
                }
            } else if (cat == "avtor") {
                if (k.avtor!!.toLowerCase().contains(iscem.toLowerCase())) {
                    koncna.add(k.id)
                }
            }
            if (cat == "vrsta") {
                if (k.vrsta!!.toLowerCase().contains(iscem.toLowerCase())) {
                    koncna.add(k.id)
                }
            }
        }



        return koncna

    }


}
