package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.søknad.DokumentIdentifikator
import no.nav.familie.ef.søknad.søknad.domain.Adresseopplysninger
import no.nav.familie.ef.søknad.søknad.domain.BooleanFelt
import no.nav.familie.ef.søknad.utils.DokumentasjonWrapper
import no.nav.familie.ef.søknad.utils.DokumentfeltUtil
import no.nav.familie.ef.søknad.utils.Språktekster
import no.nav.familie.ef.søknad.utils.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Adresseopplysninger as KontraktAdresseopplysninger

data class AdresseopplysningerData(
    val søkerBorPåRegistrertAdresse: BooleanFelt?,
    val adresseopplysninger: Adresseopplysninger?,
)

object AdresseopplysningerMapper :
    MapperMedVedlegg<AdresseopplysningerData, KontraktAdresseopplysninger>(Språktekster.Adresseopplysninger) {

    override fun mapDto(
        data: AdresseopplysningerData,
        vedlegg: Map<String, DokumentasjonWrapper>,
    ): KontraktAdresseopplysninger {
        return KontraktAdresseopplysninger(
            søkerBorPåRegistrertAdresse = data.søkerBorPåRegistrertAdresse?.tilSøknadsfelt(),
            harMeldtAdresseendring = data.adresseopplysninger?.harMeldtAdresseendring?.tilSøknadsfelt(),
            dokumentasjonAdresseendring = DokumentfeltUtil.dokumentfelt(DokumentIdentifikator.MELDT_ADRESSEENDRING, vedlegg),
        )
    }
}
