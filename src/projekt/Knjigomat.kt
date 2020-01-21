package projekt

import java.io.Serializable
import java.util.ArrayList

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table


@Entity
@Table(name = "Knjigomat")
class Knjigomat : Serializable {
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Column(name = "idKnjigomat")
    var id: Int = 0
    var ime: String? = null
    var lokacija: String? = null
    var prostor: Int = 0    //prostor na voljo
    var skupajProstor: Int = 0    //skupaj prostor na voljo v knjigomatu


}
