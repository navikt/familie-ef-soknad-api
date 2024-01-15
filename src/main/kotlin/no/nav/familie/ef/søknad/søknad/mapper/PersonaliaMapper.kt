package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.søknad.domain.Søker
import no.nav.familie.ef.søknad.utils.Språktekster.Navn
import no.nav.familie.ef.søknad.utils.Språktekster.Sivilstatus
import no.nav.familie.ef.søknad.utils.Språktekster.Statsborgerskap
import no.nav.familie.ef.søknad.utils.hentTekst
import no.nav.familie.kontrakter.ef.søknad.Adresse
import no.nav.familie.kontrakter.ef.søknad.Personalia
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.felles.Fødselsnummer
import no.nav.familie.ef.søknad.søknad.domain.Adresse as AdresseDto
import no.nav.familie.ef.søknad.utils.Språktekster.Adresse as AdresseTekst
import no.nav.familie.ef.søknad.utils.Språktekster.Fødselsnummer as FødselsnummerTekst
import no.nav.familie.ef.søknad.utils.Språktekster.Søker as SpråkTeksterSøker

object PersonaliaMapper : Mapper<Søker, Personalia>(SpråkTeksterSøker) {

    override fun mapDto(data: Søker): Personalia {
        return Personalia(
            fødselsnummer = Søknadsfelt(FødselsnummerTekst.hentTekst(), Fødselsnummer(data.fnr)),
            navn = Søknadsfelt(Navn.hentTekst(), data.forkortetNavn),
            adresse = lagAdresseSøknadsFelt(data.adresse),
            statsborgerskap = Søknadsfelt(Statsborgerskap.hentTekst(), data.statsborgerskap),
            sivilstatus = Søknadsfelt(Sivilstatus.hentTekst(), data.sivilstand),
        )
    }

    private fun lagAdresseSøknadsFelt(frontendAdresse: AdresseDto): Søknadsfelt<Adresse> {
        return Søknadsfelt(
            AdresseTekst.hentTekst(),
            Adresse(
                adresse = frontendAdresse.adresse,
                postnummer = frontendAdresse.postnummer,
                poststedsnavn = frontendAdresse.poststed ?: "",
                land = "",
            ),
        )
    }

    private fun mapAdresseTilDto(adresse: Adresse): AdresseDto {
        return AdresseDto(
            adresse = adresse.adresse ?: "",
            postnummer = adresse.postnummer,
            poststed = adresse.poststedsnavn,
        )
    }
}
