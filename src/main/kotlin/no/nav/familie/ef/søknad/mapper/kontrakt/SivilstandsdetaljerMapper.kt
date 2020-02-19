package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Sivilstatus
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.VedleggFelt
import no.nav.familie.kontrakter.ef.søknad.*
import java.time.LocalDate

object SivilstandsdetaljerMapper {

    fun mapSivilstandsdetaljer(frontendDto: SøknadDto): Sivilstandsdetaljer {
        val sivilstatus = frontendDto.sivilstatus!!
        val vedlegg = frontendDto.vedleggsliste!!
        val silvilstandsdetaljer = Sivilstandsdetaljer(
                samlivsbruddsdokumentasjon = lagSamlivsbruddsdokumentasjonSøknadsfelt(vedlegg),
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

    private fun lagSamlivsbruddsdokumentasjonSøknadsfelt(dto: List<VedleggFelt>): Søknadsfelt<Dokument>? {
        val felt = dto.filter { vedlegg -> vedlegg.navn.equals("samlivsbrudd") }.first()
        return Søknadsfelt(felt.label, Dokument(felt.dokumentId.toByteArray(), felt.navn)) //TODO: Ikke henta dokument, midlertidig test
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
