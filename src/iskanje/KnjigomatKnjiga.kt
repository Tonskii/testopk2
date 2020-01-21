package iskanje

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

import projekt.Knjiga
import projekt.Knjigomat

@Entity
@Table(name = "KnjigomatKnjiga")
class KnjigomatKnjiga {
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Column(name = "id")
    var id: Int = 0
    @get:OneToOne
    var knjiga: Knjiga? = null
    @get:OneToOne
    var masina: Knjigomat? = null
    var isJeNoter: Boolean = false
    var isJeSposojena: Boolean = false


    constructor() {

    }


    constructor(knjiga: Knjiga, masina: Knjigomat, jeNoter: Boolean, jeSposojena: Boolean) : super() {
        this.knjiga = knjiga
        this.masina = masina
        this.isJeNoter = jeNoter
        this.isJeSposojena = jeSposojena
    }


}