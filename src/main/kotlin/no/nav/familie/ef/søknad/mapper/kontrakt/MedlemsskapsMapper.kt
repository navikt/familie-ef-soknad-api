package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Medlemskap
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.PerioderBoddIUtlandet
import no.nav.familie.ef.søknad.mapper.Mapper
import no.nav.familie.ef.søknad.mapper.hentTekst
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Medlemskapsdetaljer
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Utenlandsopphold as KontraksUtenlandsopphold

object MedlemsskapsMapper : Mapper<Medlemskap, Medlemskapsdetaljer>("Opphold i Norge".hentTekst()) {


    override fun mapDto(medlemskap: Medlemskap): Medlemskapsdetaljer {
        return Medlemskapsdetaljer(medlemskap.søkerOppholderSegINorge.tilSøknadsfelt(),
                                   medlemskap.søkerBosattINorgeSisteTreÅr.tilSøknadsfelt(),
                                   Søknadsfelt("Utenlandsopphold".hentTekst(),
                                               mapUtenlansopphold(medlemskap.perioderBoddIUtlandet)))
    }

    private fun mapUtenlansopphold(perioderBoddIUtlandet: List<PerioderBoddIUtlandet>?): List<KontraksUtenlandsopphold> {
        return perioderBoddIUtlandet?.map { it ->
            KontraksUtenlandsopphold(it.periode.fra.tilSøknadsfelt(),
                                     it.periode.til.tilSøknadsfelt(),
                                     it.begrunnelse.tilSøknadsfelt())
        } ?: listOf()
    }

}


