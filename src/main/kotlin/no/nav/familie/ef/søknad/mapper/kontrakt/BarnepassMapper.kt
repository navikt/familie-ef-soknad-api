package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DatoFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.PeriodeFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.ef.søknad.mapper.Mapper
import no.nav.familie.ef.søknad.mapper.Språktekster.Barnepassordninger
import no.nav.familie.ef.søknad.mapper.Språktekster.OmBarnepassordning
import no.nav.familie.ef.søknad.mapper.hentTekst
import no.nav.familie.ef.søknad.mapper.tilDesimaltall
import no.nav.familie.ef.søknad.mapper.tilLocalDate
import no.nav.familie.ef.søknad.mapper.tilNullableTekstFelt
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Barnepass
import no.nav.familie.kontrakter.ef.søknad.BarnepassOrdning
import no.nav.familie.kontrakter.ef.søknad.Datoperiode
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Barnepass as BarnepassDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BarnepassOrdning as BarnepassOrdningDto
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
