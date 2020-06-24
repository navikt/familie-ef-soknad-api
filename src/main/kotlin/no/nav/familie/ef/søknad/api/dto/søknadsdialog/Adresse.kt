package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Adresse(val adresse: String,
                   val postnummer: String,
                   val poststed: String?)

// TODO mangler mapping av data fra ui? : adressetillegg? Kommune?