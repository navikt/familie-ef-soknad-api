package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BooleanFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.OpplysningerOmAdresse
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.DokumentfeltUtil
import no.nav.familie.ef.søknad.mapper.MapperMedVedlegg
import no.nav.familie.ef.søknad.mapper.Språktekster
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.OpplysningerOmAdresse as KontraktOpplysningerOmAdresse

data class OpplysningerOmAdresseData(
    val søkerBorPåRegistrertAdresse: BooleanFelt?,
    val opplysningerOmAdresse: OpplysningerOmAdresse?
)

object OpplysningerOmAdresseMapper :
    MapperMedVedlegg<OpplysningerOmAdresseData, KontraktOpplysningerOmAdresse>(Språktekster.Bosituasjon) {

    override fun mapDto(
        data: OpplysningerOmAdresseData,
        vedlegg: Map<String, DokumentasjonWrapper>
    ): KontraktOpplysningerOmAdresse {
        return KontraktOpplysningerOmAdresse(
            søkerBorPåRegistrertAdresse = data.søkerBorPåRegistrertAdresse?.tilSøknadsfelt(),
            harMeldtFlytteendring = data.opplysningerOmAdresse?.harMeldtFlytteendring?.tilSøknadsfelt(),
            DokumentfeltUtil.dokumentfelt(DokumentIdentifikator.MELDT_FLYTTEENDRING, vedlegg)
        )
    }

}
