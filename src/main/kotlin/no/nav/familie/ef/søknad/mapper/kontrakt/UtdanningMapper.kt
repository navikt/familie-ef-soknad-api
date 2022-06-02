package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TidligereUtdanning
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.UnderUtdanning
import no.nav.familie.ef.søknad.mapper.Mapper
import no.nav.familie.ef.søknad.mapper.Språktekster.NårSkalDuVæreElevStudent
import no.nav.familie.ef.søknad.mapper.Språktekster.NårVarDuElevStudent
import no.nav.familie.ef.søknad.mapper.Språktekster.Utdanning
import no.nav.familie.ef.søknad.mapper.Språktekster.UtdanningenDuSkalTa
import no.nav.familie.ef.søknad.mapper.hentTekst
import no.nav.familie.ef.søknad.mapper.tilDesimaltall
import no.nav.familie.ef.søknad.mapper.tilHeltall
import no.nav.familie.ef.søknad.mapper.tilLocalDate
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Datoperiode
import no.nav.familie.kontrakter.ef.søknad.GjeldendeUtdanning
import no.nav.familie.kontrakter.ef.søknad.MånedÅrPeriode
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.ef.søknad.mapper.Språktekster.TidligereUtdanning as SpråkTeksterTidligereUtdanning
import no.nav.familie.kontrakter.ef.søknad.TidligereUtdanning as TidligereUtdanningKontrakt
import no.nav.familie.kontrakter.ef.søknad.UnderUtdanning as UnderUtdanningKontrakt

object UtdanningMapper : Mapper<UnderUtdanning, UnderUtdanningKontrakt>(UtdanningenDuSkalTa) {

    override fun mapDto(underUtdanning: UnderUtdanning): UnderUtdanningKontrakt {
        return UnderUtdanningKontrakt(
            skoleUtdanningssted = underUtdanning.skoleUtdanningssted.tilSøknadsfelt(),
            utdanning = null,
            gjeldendeUtdanning =
            Søknadsfelt(
                Utdanning.hentTekst(),
                GjeldendeUtdanning(
                    underUtdanning.linjeKursGrad.tilSøknadsfelt(),
                    Søknadsfelt(
                        NårSkalDuVæreElevStudent.hentTekst(),
                        Datoperiode(
                            underUtdanning.periode.fra.tilLocalDate(),
                            underUtdanning.periode.til.tilLocalDate()
                        )
                    )
                )
            ),
            offentligEllerPrivat = underUtdanning.offentligEllerPrivat.tilSøknadsfelt(),
            hvorMyeSkalDuStudere = underUtdanning.arbeidsmengde?.tilSøknadsfelt(String::tilHeltall),
            heltidEllerDeltid = underUtdanning.heltidEllerDeltid.tilSøknadsfelt(),
            hvaErMåletMedUtdanningen = underUtdanning.målMedUtdanning?.tilSøknadsfelt(),
            utdanningEtterGrunnskolen = underUtdanning.harTattUtdanningEtterGrunnskolen.tilSøknadsfelt(),
            tidligereUtdanninger = underUtdanning.tidligereUtdanning?.let { mapTidligereUtdanning(it) },
            semesteravgift = mapUtgifterTilUtdanning(underUtdanning.semesteravgift),
            studieavgift = mapUtgifterTilUtdanning(underUtdanning.studieavgift),
            eksamensgebyr = mapUtgifterTilUtdanning(underUtdanning.eksamensgebyr)
        )
    }

    private fun mapTidligereUtdanning(tidligereUtdanning: List<TidligereUtdanning>): Søknadsfelt<List<TidligereUtdanningKontrakt>> {
        val tidligereUtdanningList = tidligereUtdanning.map {
            TidligereUtdanningKontrakt(
                it.linjeKursGrad.tilSøknadsfelt(),
                Søknadsfelt(
                    NårVarDuElevStudent.hentTekst(),
                    MånedÅrPeriode(
                        it.periode.fra.tilLocalDate().month,
                        it.periode.fra.tilLocalDate().year,
                        it.periode.til.tilLocalDate().month,
                        it.periode.til.tilLocalDate().year
                    )
                )
            )
        }
        return Søknadsfelt(SpråkTeksterTidligereUtdanning.hentTekst(), tidligereUtdanningList)
    }

    private fun mapUtgifterTilUtdanning(utgift: TekstFelt?): Søknadsfelt<Double>? {
        return utgift?.let {
            return if (it.verdi.isNotBlank()) {
                utgift.tilSøknadsfelt(String::tilDesimaltall)
            } else {
                null
            }
        }
    }
}
