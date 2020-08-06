package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Medlemskap
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.PerioderBoddIUtlandet
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Medlemskapsdetaljer
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import org.slf4j.LoggerFactory
import no.nav.familie.kontrakter.ef.søknad.Utenlandsopphold as KontraksUtenlandsopphold

object MedlemsskapsMapper {

    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun mapMedlemskap(medlemskap: Medlemskap): Medlemskapsdetaljer {
        try {

            return Medlemskapsdetaljer(medlemskap.søkerOppholderSegINorge.tilSøknadsfelt(),
                                       medlemskap.søkerBosattINorgeSisteTreÅr.tilSøknadsfelt(),
                                       Søknadsfelt("Utenlandsopphold", mapUtenlansopphold(medlemskap.perioderBoddIUtlandet)))
        } catch (e: Exception) {
            secureLogger.error("Feil ved mapping av madlemskap: $medlemskap")
            throw e
        }
    }

    private fun mapUtenlansopphold(perioderBoddIUtlandet: List<PerioderBoddIUtlandet>?): List<KontraksUtenlandsopphold> {
        return perioderBoddIUtlandet?.map { it ->
            KontraksUtenlandsopphold(it.periode.fra.tilSøknadsfelt(),
                                     it.periode.til.tilSøknadsfelt(),
                                     it.begrunnelse.tilSøknadsfelt())
        } ?: listOf()
    }

}


