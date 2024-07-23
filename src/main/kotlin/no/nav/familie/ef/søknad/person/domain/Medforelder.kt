package no.nav.familie.ef.søknad.person.domain

data class Medforelder(
    val navn: String? = "",
    val harAdressesperre: Boolean,
    val død: Boolean = false,
    val ident: String = "",
    val alder: Int,
) {
    override fun toString(): String = "MedForelder(harAdressesperre=$harAdressesperre, død=$død, alder=$alder)"
}
