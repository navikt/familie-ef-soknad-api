package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.søknad.domain.BooleanFelt
import no.nav.familie.ef.søknad.søknad.domain.DatoFelt
import no.nav.familie.ef.søknad.søknad.domain.Medlemskap
import no.nav.familie.ef.søknad.søknad.domain.PeriodeFelt
import no.nav.familie.ef.søknad.søknad.domain.TekstFelt
import no.nav.familie.ef.søknad.søknad.domain.Utenlandsperiode
import no.nav.familie.ef.søknad.utils.Språktekster
import no.nav.familie.ef.søknad.utils.hentTekst
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
                mapUtenlansopphold(data.utenlandsperiode),
            ),
        )
    }

    private fun mapUtenlansopphold(utenlandsperiode: List<Utenlandsperiode>?): List<KontraksUtenlandsopphold> {
        return utenlandsperiode?.map { it ->
            KontraksUtenlandsopphold(
                fradato = it.periode.fra.tilSøknadsfelt(),
                tildato = it.periode.til.tilSøknadsfelt(),
                land = it.land?.tilSøknadsfelt(),
                årsakUtenlandsopphold = it.begrunnelse.tilSøknadsfelt(),
                personidentUtland = it.personident?.tilSøknadsfelt(),
                adresseUtland = it.adresse?.tilSøknadsfelt(),
            )
        } ?: listOf()
    }

    fun mapTilDto(medlemskapsdetaljer: Medlemskapsdetaljer): Medlemskap {
        return Medlemskap(
            utenlandsperiode = medlemskapsdetaljer.utenlandsopphold?.verdi?.map {
                Utenlandsperiode(
                    begrunnelse = TekstFelt(it.årsakUtenlandsopphold.label, it.årsakUtenlandsopphold.verdi),
                    periode = PeriodeFelt(
                        fra = DatoFelt(it.fradato.label, it.fradato.verdi.toString()),
                        til = DatoFelt(it.tildato.label, it.tildato.verdi.toString()),
                        label = null,
                    ),
                    land = it.land.tilNullableTekstFelt(),
                    personident = it.personidentUtland.tilNullableTekstFelt(),
                    adresse = it.adresseUtland.tilNullableTekstFelt(),
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
