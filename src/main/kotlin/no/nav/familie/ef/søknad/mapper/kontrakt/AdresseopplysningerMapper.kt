package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Adresseopplysninger
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BooleanFelt
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.DokumentfeltUtil
import no.nav.familie.ef.søknad.mapper.MapperMedVedlegg
import no.nav.familie.ef.søknad.mapper.Språktekster
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Adresseopplysninger as KontraktAdresseopplysninger

data class AdresseopplysningerData(
    val søkerBorPåRegistrertAdresse: BooleanFelt?,
    val adresseopplysninger: Adresseopplysninger?
)

object AdresseopplysningerMapper :
    MapperMedVedlegg<AdresseopplysningerData, KontraktAdresseopplysninger>(Språktekster.Bosituasjon) {

    override fun mapDto(
        data: AdresseopplysningerData,
        vedlegg: Map<String, DokumentasjonWrapper>
    ): KontraktAdresseopplysninger {
        return KontraktAdresseopplysninger(
            søkerBorPåRegistrertAdresse = data.søkerBorPåRegistrertAdresse?.tilSøknadsfelt(),
            harMeldtFlytteendring = data.adresseopplysninger?.harMeldtFlytteendring?.tilSøknadsfelt(),
            DokumentfeltUtil.dokumentfelt(DokumentIdentifikator.MELDT_FLYTTEENDRING, vedlegg)
        )
    }

}
