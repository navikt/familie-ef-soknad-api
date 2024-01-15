package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.søknad.domain.DatoFelt
import no.nav.familie.ef.søknad.søknad.domain.PeriodeFelt
import no.nav.familie.ef.søknad.søknad.domain.TekstFelt
import no.nav.familie.ef.søknad.utils.Språktekster.Barnepassordninger
import no.nav.familie.ef.søknad.utils.Språktekster.OmBarnepassordning
import no.nav.familie.ef.søknad.utils.hentTekst
import no.nav.familie.ef.søknad.utils.tilDesimaltall
import no.nav.familie.ef.søknad.utils.tilLocalDate
import no.nav.familie.ef.søknad.utils.tilNullableTekstFelt
import no.nav.familie.ef.søknad.utils.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Barnepass
import no.nav.familie.kontrakter.ef.søknad.BarnepassOrdning
import no.nav.familie.kontrakter.ef.søknad.Datoperiode
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.ef.søknad.søknad.domain.Barnepass as BarnepassDto
import no.nav.familie.ef.søknad.søknad.domain.BarnepassOrdning as BarnepassOrdningDto

object BarnepassMapper : Mapper<BarnepassDto, Barnepass>(OmBarnepassordning) {

    override fun mapDto(data: BarnepassDto): Barnepass {
        return Barnepass(
            årsakBarnepass = data.årsakBarnepass?.tilSøknadsfelt(),
            barnepassordninger = Søknadsfelt(
                Barnepassordninger.hentTekst(),
                data.barnepassordninger.map {
                    BarnepassOrdning(
                        hvaSlagsBarnepassOrdning = it.hvaSlagsBarnepassOrdning.tilSøknadsfelt(),
                        navn = it.navn.tilSøknadsfelt(),
                        datoperiode = Søknadsfelt(
                            it.periode.label
                                ?: error("Savner label"),
                            Datoperiode(
                                it.periode.fra.tilLocalDate(),
                                it.periode.til.tilLocalDate(),
                            ),
                        ),
                        belop = it.belop.tilSøknadsfelt(String::tilDesimaltall),
                    )
                },
            ),
        )
    }

    fun mapTilDto(barnepass: Barnepass?): BarnepassDto {
        return BarnepassDto(
            årsakBarnepass = barnepass?.årsakBarnepass.tilNullableTekstFelt(),
            barnepassordninger = mapBarnepassordningerTilDto(barnepass?.barnepassordninger?.verdi ?: emptyList()),
        )
    }

    fun mapBarnepassordningerTilDto(barnepassOrdning: List<BarnepassOrdning>): List<BarnepassOrdningDto> {
        return barnepassOrdning.map {
            BarnepassOrdningDto(
                hvaSlagsBarnepassOrdning = TekstFelt(it.hvaSlagsBarnepassOrdning.label, it.hvaSlagsBarnepassOrdning.verdi, it.hvaSlagsBarnepassOrdning.svarId),
                navn = TekstFelt(it.navn.label, it.navn.verdi, it.navn.svarId),
                periode = PeriodeFelt(
                    it.datoperiode?.label,
                    DatoFelt(it.datoperiode?.label ?: "Startdato", it.datoperiode?.verdi?.fra.toString()),
                    DatoFelt(it.datoperiode?.label ?: "Sluttdato", it.datoperiode?.verdi?.til.toString()),
                ),
                belop = TekstFelt(it.belop.label, it.belop.verdi.toString(), it.belop.svarId.toString()),
            )
        }
    }
}
