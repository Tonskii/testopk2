package projekt

import java.util.ArrayList
import java.util.Date

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "Narocilo")
class Narocilo {


    @get:Id
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Column(name = "idNarocilo")
    var id: Int = 0
    var stanje: Boolean = false

    var datumOd: Date
    var datumDo: Date
    @get:OneToOne
    var uporabnik: Uporabnik
    @get:OneToOne
    var knjigomat: Knjigomat
    @get:OneToOne
    var knjiga: Knjiga


}
