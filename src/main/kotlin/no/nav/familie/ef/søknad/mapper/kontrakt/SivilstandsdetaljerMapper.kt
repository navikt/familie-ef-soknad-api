package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Sivilstatus
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.VedleggFelt
import no.nav.familie.kontrakter.ef.søknad.*
import java.time.LocalDate

object SivilstandsdetaljerMapper {

    fun mapSivilstandsdetaljer(frontendDto: SøknadDto, dokumentMap: Map<String, Dokument>): Sivilstandsdetaljer {
        val sivilstatus = frontendDto.sivilstatus!!
        val silvilstandsdetaljer = Sivilstandsdetaljer(
                samlivsbruddsdokumentasjon = lagSamlivsbruddsdokumentasjonSøknadsfelt(dokumentMap),
                samlivsbruddsdato = lagSamlivsbruddsdatoSøknadsfelt(sivilstatus))
        return silvilstandsdetaljer
    }

    private fun lagGiftIUtlandetSøknadsfelt(dto: Sivilstatus): Søknadsfelt<Boolean>? = null

    private fun lagGiftIUtlandetDokumentasjonSøknadsfelt(dto: List<VedleggFelt>): Søknadsfelt<Dokument>? = null

    private fun lagSeparertEllerSkiltIUtlandetSøknadsfelt(dto: Sivilstatus): Søknadsfelt<Boolean>? = null

    private fun lagSeparertEllerSkiltIUtlandetDokumentasjonSøknadsfelt(dto: List<VedleggFelt>): Søknadsfelt<Dokument>? = null

    private fun lagSøktOmSkilsmisseSeparasjonSøknadsfelt(dto: Sivilstatus): Søknadsfelt<Boolean>? = null

    private fun lagSøknadsdatoSøknadsfelt(dto: Sivilstatus): Søknadsfelt<LocalDate>? = null

    private fun lagSeparasjonsbekreftelseSøknadsfelt(dto: List<VedleggFelt>): Søknadsfelt<Dokument>? = null

    private fun lagÅrsakEnsligSøknadsfelt(dto: Sivilstatus): Søknadsfelt<String>? = null

    private fun lagSamlivsbruddsdokumentasjonSøknadsfelt(dokumentMap: Map<String, Dokument>): Søknadsfelt<Dokument>? {
        val dokument = dokumentMap["samlivsbrudd"]!!
        return Søknadsfelt(dokument.tittel, dokument)
    }

    private fun lagSamlivsbruddsdatoSøknadsfelt(dto: Sivilstatus): Søknadsfelt<LocalDate>? =
        try {
            val felt = dto.datoForSamlivsbrudd!!
            Søknadsfelt(felt.label, felt.verdi)
            Søknadsfelt(felt.label, felt.verdi)
        } catch (e: NullPointerException) {
            null
        }

    private fun lagFraflytningsdatoSøknadsfelt(dto: Sivilstatus): Søknadsfelt<LocalDate>? = null

    private fun lagSpesifikasjonAnnetSøknadsfelt(dto: Sivilstatus): Søknadsfelt<String>? = null

    private fun lagEndringSamværsordningDatoSøknadsfelt(dto: Sivilstatus): Søknadsfelt<LocalDate>? = null
}
