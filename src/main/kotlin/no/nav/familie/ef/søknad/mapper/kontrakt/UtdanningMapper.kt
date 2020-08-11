package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TidligereUtdanning
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.UnderUtdanning
import no.nav.familie.ef.søknad.mapper.*
import no.nav.familie.kontrakter.ef.søknad.Periode
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Utdanning
import no.nav.familie.kontrakter.ef.søknad.UnderUtdanning as UnderUtdanningKontrakt

object UtdanningMapper : Mapper<UnderUtdanning, UnderUtdanningKontrakt>("Utdanningen du skal ta") {

    override fun mapDto(underUtdanning: UnderUtdanning): UnderUtdanningKontrakt {
        return UnderUtdanningKontrakt(skoleUtdanningssted = underUtdanning.skoleUtdanningssted.tilSøknadsfelt(),
                                      utdanning =
                                      Søknadsfelt("Utdanning",
                                                  Utdanning(underUtdanning.linjeKursGrad.tilSøknadsfelt(),
                                                            Søknadsfelt("Når skal du være elev/student?",
                                                                        Periode(underUtdanning.periode.fra.tilLocalDate().month,
                                                                                underUtdanning.periode.fra.tilLocalDate().year,
                                                                                underUtdanning.periode.til.tilLocalDate().month,
                                                                                underUtdanning.periode.til.tilLocalDate().year)))),
                                      offentligEllerPrivat = underUtdanning.offentligEllerPrivat.tilSøknadsfelt(),
                                      hvorMyeSkalDuStudere = underUtdanning.arbeidsmengde?.tilSøknadsfelt(String::tilHeltall),
                                      heltidEllerDeltid = underUtdanning.heltidEllerDeltid.tilSøknadsfelt(),
                                      hvaErMåletMedUtdanningen = underUtdanning.målMedUtdanning?.tilSøknadsfelt(),
                                      utdanningEtterGrunnskolen = underUtdanning.harTattUtdanningEtterGrunnskolen.tilSøknadsfelt(),
                                      tidligereUtdanninger = underUtdanning.tidligereUtdanning?.let { mapTidligereUtdanning(it) },
                                      semesteravgift = underUtdanning.semesteravgift?.tilSøknadsfelt(String::tilDesimaltall),
                                      studieavgift = underUtdanning.studieavgift?.tilSøknadsfelt(String::tilDesimaltall),
                                      eksamensgebyr = underUtdanning.eksamensgebyr?.tilSøknadsfelt(String::tilDesimaltall)
        )
    }

    private fun mapTidligereUtdanning(tidligereUtdanning: List<TidligereUtdanning>): Søknadsfelt<List<Utdanning>> {
        val tidligereUtdanningList = tidligereUtdanning.map {
            Utdanning(it.linjeKursGrad.tilSøknadsfelt(),
                      Søknadsfelt("Når var du elev/student?",
                                  Periode(it.periode.fra.tilLocalDate().month,
                                          it.periode.fra.tilLocalDate().year,
                                          it.periode.til.tilLocalDate().month,
                                          it.periode.til.tilLocalDate().year)))
        }
        return Søknadsfelt("Tidligere Utdanning", tidligereUtdanningList)
    }

}
