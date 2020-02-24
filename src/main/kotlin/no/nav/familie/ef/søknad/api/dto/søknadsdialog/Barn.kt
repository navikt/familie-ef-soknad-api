package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Barn(val alder: Int? = null,
                val fnr: String? = null,
                val fødselsdato: String? = null,
                val harSammeAdresse: Boolean? = null,
                val id: String? = null,
                val lagtTil: Boolean? = null,
                val navn: String? = null,
                val personnummer: String? = null,
                val ufødt: Boolean? = null)
