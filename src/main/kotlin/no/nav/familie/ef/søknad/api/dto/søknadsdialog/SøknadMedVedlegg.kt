package no.nav.familie.ef.søknad.api.dto.søknadsdialog
import no.nav.familie.kontrakter.ef.søknad.Dokumentasjonsbehov

data class SøknadMedVedlegg(val søknadId: String, val dokumentasjonsbehov: List<Dokumentasjonsbehov>,val åpenInnsending: List<ÅpenInnsending>)