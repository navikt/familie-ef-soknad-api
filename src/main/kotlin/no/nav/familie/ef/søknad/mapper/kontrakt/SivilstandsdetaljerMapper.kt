package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Sivilstatus
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.VedleggFelt
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsdetaljer
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import java.time.LocalDate

object SivilstandsdetaljerMapper {

    fun mapSivilstandsdetaljer(frontendDto: SøknadDto, dokumentMap: Map<String, Dokument>): Sivilstandsdetaljer {
        val sivilstatus = frontendDto.sivilstatus
        return Sivilstandsdetaljer(samlivsbruddsdokumentasjon = lagSamlivsbruddsdokumentasjonSøknadsfelt(dokumentMap),
                                   samlivsbruddsdato = sivilstatus.datoForSamlivsbrudd?.tilSøknadsfelt())
    }

    private fun lagGiftIUtlandetSøknadsfelt(dto: Sivilstatus): Søknadsfelt<Boolean>? = null

    private fun lagGiftIUtlandetDokumentasjonSøknadsfelt(dto: List<VedleggFelt>): Søknadsfelt<Dokument>? = null

    private fun lagSeparertEllerSkiltIUtlandetSøknadsfelt(dto: Sivilstatus): Søknadsfelt<Boolean>? = null

    private fun lagSeparertEllerSkiltIUtlandetDokumentasjonSøknadsfelt(dto: List<VedleggFelt>): Søknadsfelt<Dokument>? = null

    private fun lagSøktOmSkilsmisseSeparasjonSøknadsfelt(dto: Sivilstatus): Søknadsfelt<Boolean>? = null

    private fun lagSøknadsdatoSøknadsfelt(dto: Sivilstatus): Søknadsfelt<LocalDate>? = null

    private fun lagSeparasjonsbekreftelseSøknadsfelt(dto: List<VedleggFelt>): Søknadsfelt<Dokument>? = null

    private fun lagÅrsakEnsligSøknadsfelt(dto: Sivilstatus): Søknadsfelt<String>? = null

    private fun lagSamlivsbruddsdokumentasjonSøknadsfelt(dokumenter: Map<String, Dokument>): Søknadsfelt<Dokument>? {
        return dokumentfelt("samlivsbrudd", dokumenter)
    }

    private fun lagFraflytningsdatoSøknadsfelt(dto: Sivilstatus): Søknadsfelt<LocalDate>? = null

    private fun lagSpesifikasjonAnnetSøknadsfelt(dto: Sivilstatus): Søknadsfelt<String>? = null

    private fun lagEndringSamværsordningDatoSøknadsfelt(dto: Sivilstatus): Søknadsfelt<LocalDate>? = null
}
