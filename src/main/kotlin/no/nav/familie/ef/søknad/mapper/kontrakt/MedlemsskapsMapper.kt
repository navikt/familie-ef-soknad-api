package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.PerioderBoddIUtlandet
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Medlemskapsdetaljer
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Utenlandsopphold as KontraksUtenlandsopphold

object MedlemsskapsMapper {
    fun mapMedlemskap(frontendDto: SøknadDto): Medlemskapsdetaljer {
        val medlemskap = frontendDto.medlemskap
        return Medlemskapsdetaljer(medlemskap.søkerOppholderSegINorge.tilSøknadsfelt(),
                                   medlemskap.søkerBosattINorgeSisteTreÅr.tilSøknadsfelt(),
                                   Søknadsfelt("Utenlandsopphold", mapUtenlansopphold(medlemskap.perioderBoddIUtlandet)))
    }

    private fun mapUtenlansopphold(perioderBoddIUtlandet: List<PerioderBoddIUtlandet>?): List<KontraksUtenlandsopphold> {
        return perioderBoddIUtlandet?.map { it ->
            KontraksUtenlandsopphold(it.periode.fra.tilSøknadsfelt(),
                                     it.periode.til.tilSøknadsfelt(),
                                     it.begrunnelse.tilSøknadsfelt())
        } ?: listOf()
    }

}


