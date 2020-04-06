package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Barn(val alder: TekstFelt? = null,
                val fnr: TekstFelt? = null,
                val fødselsdato: TekstFelt? = null,
                val harSammeAdresse: BooleanFelt? = null,
                val id: String? = null,
                val lagtTil: BooleanFelt? = null,
                val navn: TekstFelt? = null,
                val personnummer: TekstFelt? = null,
                val ufødt: BooleanFelt? = null)



