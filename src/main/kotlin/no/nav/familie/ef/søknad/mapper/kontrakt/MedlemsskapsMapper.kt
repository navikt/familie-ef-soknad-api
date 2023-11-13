package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.*
import no.nav.familie.ef.søknad.mapper.Mapper
import no.nav.familie.ef.søknad.mapper.Språktekster
import no.nav.familie.ef.søknad.mapper.hentTekst
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Medlemskapsdetaljer
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsdetaljer
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Utenlandsopphold as KontraksUtenlandsopphold

object MedlemsskapsMapper : Mapper<Medlemskap, Medlemskapsdetaljer>(Språktekster.OppholdINorge) {

    override fun mapDto(data: Medlemskap): Medlemskapsdetaljer {
        return Medlemskapsdetaljer(
            oppholderDuDegINorge = data.søkerOppholderSegINorge.tilSøknadsfelt(),
            oppholdsland = data.oppholdsland?.tilSøknadsfelt(),
            bosattNorgeSisteÅrene = data.søkerBosattINorgeSisteTreÅr.tilSøknadsfelt(),
            utenlandsopphold = Søknadsfelt(
                Språktekster.Utenlandsopphold.hentTekst(),
                mapUtenlansopphold(data.perioderBoddIUtlandet),
            ),
        )
    }

    private fun mapUtenlansopphold(perioderBoddIUtlandet: List<PerioderBoddIUtlandet>?): List<KontraksUtenlandsopphold> {
        return perioderBoddIUtlandet?.map { it ->
            KontraksUtenlandsopphold(
                fradato = it.periode.fra.tilSøknadsfelt(),
                tildato = it.periode.til.tilSøknadsfelt(),
                land = it.land?.tilSøknadsfelt(),
                årsakUtenlandsopphold = it.begrunnelse.tilSøknadsfelt(),
            )
        } ?: listOf()
    }

    fun mapTilDto(medlemskapsdetaljer: Medlemskapsdetaljer): Medlemskap {
        return Medlemskap(
            perioderBoddIUtlandet = medlemskapsdetaljer.utenlandsopphold?.verdi?.map {
                PerioderBoddIUtlandet(
                    begrunnelse = TekstFelt(it.årsakUtenlandsopphold.label, it.årsakUtenlandsopphold.verdi),
                    periode = PeriodeFelt(
                        fra = DatoFelt(it.fradato.label, it.fradato.verdi.toString()),
                        til = DatoFelt(it.tildato.label, it.tildato.verdi.toString()),
                        label = null
                    ),
                    land = it.land.tilTekstFelt()
                )
            },
            søkerBosattINorgeSisteTreÅr = BooleanFelt(
                medlemskapsdetaljer.bosattNorgeSisteÅrene.label,
                medlemskapsdetaljer.bosattNorgeSisteÅrene.verdi
            ),
            oppholdsland = medlemskapsdetaljer.oppholdsland.tilTekstFelt(),
            søkerOppholderSegINorge = BooleanFelt(
                medlemskapsdetaljer.oppholderDuDegINorge.label,
                medlemskapsdetaljer.oppholderDuDegINorge.verdi
            ),
        )

    }
}
