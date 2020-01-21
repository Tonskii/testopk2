package projekt

import java.util.Date

import javax.jws.Oneway
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table


@Entity
@Table(name = "Izposoja")
class Izposoja {

    @get:Id
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Column(name = "idIzposoja")
    var id: Int = 0
    var isStanje: Boolean = false
    var datumOd: Date
    var datumDo: Date
    @get:OneToOne
    var knjiga: Knjiga
    @get:OneToOne
    var upo: Uporabnik


}
