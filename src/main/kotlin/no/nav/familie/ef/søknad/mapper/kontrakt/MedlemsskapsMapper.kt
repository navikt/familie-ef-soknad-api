package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.PerioderBoddIUtlandet
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.Medlemskapsdetaljer
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Utenlandsopphold as KontraksUtenlandsopphold

object MedlemsskapsMapper {
    fun mapMedlemskap(frontendDto: SøknadDto, dokumenter: Map<String, Dokument>): Medlemskapsdetaljer {
        val medlemskap = frontendDto.medlemskap
        return Medlemskapsdetaljer(Søknadsfelt(medlemskap.søkerOppholderSegINorge?.label
                                               ?: error("missing søkerOppholderSegINorge label"),
                                               medlemskap.søkerOppholderSegINorge.verdi),
                                   Søknadsfelt(medlemskap.søkerBosattINorgeSisteTreÅr?.label
                                               ?: error("mangler label <Har du bodd i Norge de siste tre årene?>"),
                                               medlemskap.søkerBosattINorgeSisteTreÅr?.verdi),
                                   Søknadsfelt("Utenlandsopphold", mapUtenlansopphold(medlemskap.perioderBoddIUtlandet)),
                                    // flyktningsatus skal fjernes fra kontrakter - ingen mapping
                                   Søknadsfelt("Har du flyktningsatus hos Utlendingsdirektoratet?", true),
                                   dokumentfelt("flyktningdokumentasjon", dokumenter))
    }

    private fun mapUtenlansopphold(perioderBoddIUtlandet: List<PerioderBoddIUtlandet>?): List<KontraksUtenlandsopphold> {
        return perioderBoddIUtlandet?.map { it ->
            KontraksUtenlandsopphold(Søknadsfelt(it.periode.fra.label, it.periode.fra.verdi),
                                     Søknadsfelt(it.periode.til.label, it.periode.fra.verdi),
                                     Søknadsfelt(it.begrunnelse.label, it.begrunnelse.verdi))
        } ?: listOf()
    }

    private fun dokumentfelt(dokumentNavn: String, dokumenter: Map<String, Dokument>): Søknadsfelt<Dokument>? {
        val dokument = dokumenter[dokumentNavn]
        return dokument?.let {
            Søknadsfelt(dokument.tittel, dokument)
        }
    }
}


