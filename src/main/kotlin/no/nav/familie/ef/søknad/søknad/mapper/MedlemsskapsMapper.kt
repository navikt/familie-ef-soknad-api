package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.søknad.domain.BooleanFelt
import no.nav.familie.ef.søknad.søknad.domain.DatoFelt
import no.nav.familie.ef.søknad.søknad.domain.Medlemskap
import no.nav.familie.ef.søknad.søknad.domain.PeriodeFelt
import no.nav.familie.ef.søknad.søknad.domain.TekstFelt
import no.nav.familie.ef.søknad.søknad.domain.Utenlandsperiode
import no.nav.familie.ef.søknad.utils.Språktekster
import no.nav.familie.ef.søknad.utils.hentTekst
import no.nav.familie.ef.søknad.utils.tilNullableBooleanFelt
import no.nav.familie.ef.søknad.utils.tilNullableTekstFelt
import no.nav.familie.ef.søknad.utils.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Medlemskapsdetaljer
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

    private fun mapUtenlansopphold(perioderBoddIUtlandet: List<Utenlandsperiode>?): List<KontraksUtenlandsopphold> {
        return perioderBoddIUtlandet?.map { it ->
            KontraksUtenlandsopphold(
                fradato = it.periode.fra.tilSøknadsfelt(),
                tildato = it.periode.til.tilSøknadsfelt(),
                land = it.land?.tilSøknadsfelt(),
                årsakUtenlandsopphold = it.begrunnelse.tilSøknadsfelt(),
                personidentEøsLand = it.personidentEøsLand?.tilSøknadsfelt(),
                adresseEøsLand = it.adresseEøsLand?.tilSøknadsfelt(),
            )
        } ?: listOf()
    }

    fun mapTilDto(medlemskapsdetaljer: Medlemskapsdetaljer): Medlemskap {
        return Medlemskap(
            perioderBoddIUtlandet = medlemskapsdetaljer.utenlandsopphold?.verdi?.map {
                Utenlandsperiode(
                    begrunnelse = TekstFelt(it.årsakUtenlandsopphold.label, it.årsakUtenlandsopphold.verdi),
                    periode = PeriodeFelt(
                        fra = DatoFelt(it.fradato.label, it.fradato.verdi.toString()),
                        til = DatoFelt(it.tildato.label, it.tildato.verdi.toString()),
                        label = null,
                    ),
                    land = it.land.tilNullableTekstFelt(),
                    personidentEøsLand = it.personidentEøsLand.tilNullableTekstFelt(),
                    adresseEøsLand = it.adresseEøsLand.tilNullableTekstFelt(),
                    erEøsLand = it.erEøsLand.tilNullableBooleanFelt(),
                    kanIkkeOppgiPersonident = it.kanIkkeOppgiPersonident.tilNullableBooleanFelt(),
                )
            },
            søkerBosattINorgeSisteTreÅr = BooleanFelt(
                medlemskapsdetaljer.bosattNorgeSisteÅrene.label,
                medlemskapsdetaljer.bosattNorgeSisteÅrene.verdi,
            ),
            oppholdsland = medlemskapsdetaljer.oppholdsland.tilNullableTekstFelt(),
            søkerOppholderSegINorge = BooleanFelt(
                medlemskapsdetaljer.oppholderDuDegINorge.label,
                medlemskapsdetaljer.oppholderDuDegINorge.verdi,
            ),
        )
    }
}
