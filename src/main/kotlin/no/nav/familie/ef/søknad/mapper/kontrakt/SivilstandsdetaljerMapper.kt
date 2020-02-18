package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Sivilstatus
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.VedleggFelt
import no.nav.familie.ef.søknad.mapper.kontrakt.SøknadMapper.dokumentfelt
import no.nav.familie.kontrakter.ef.søknad.*
import java.time.LocalDate

object SivilstandsdetaljerMapper {

    @Suppress("LongLine")
    fun sivilstandsdetaljer(): Sivilstandsdetaljer {
        return Sivilstandsdetaljer(Søknadsfelt("Er du gift uten at dette er formelt registrert eller godkjent i Norge?",
                                               true),
                                   dokumentfelt(
                                           "giftIUtlandetDokumentasjon"),
                                   Søknadsfelt("Er du separert eller skilt uten at dette er formelt registrert eller godkjent i Norge?",
                                               true),
                                   dokumentfelt(
                                           "separertEllerSkiltIUtlandetDokumentasjon"),
                                   Søknadsfelt("Har dere søkt om separasjon, søkt om skilsmisse eller reist sak for domstolen?",
                                               true),
                                   Søknadsfelt("Når søkte dere eller reiste sak?", LocalDate.of(2015, 12, 23)),
                                   dokumentfelt(
                                           "Skilsmisse- eller separasjonsbevilling"),
                                   Søknadsfelt("Hva er grunnen til at du er alene med barn?",
                                               "Endring i samværsordning"),
                                   dokumentfelt(
                                           "Erklæring om samlivsbrudd"),
                                   Søknadsfelt("Dato for samlivsbrudd", LocalDate.of(2014, 10, 3)),
                                   Søknadsfelt("Når flyttet dere fra hverandre?", LocalDate.of(2014, 10, 4)),
                                   Søknadsfelt("Spesifiser grunnen til at du er alene med barn?",
                                               "Trives best alene"),
                                   Søknadsfelt("Når skjedde endringen / når skal endringen skje?",
                                               LocalDate.of(2013, 4, 17)))
    }

    fun mapSivilstandsdetaljer(frontendDto: SøknadDto): Søknadsfelt<Sivilstandsdetaljer> {
        val sivilstatus = frontendDto.sivilstatus!!
        val vedlegg = frontendDto.vedleggsliste!!
        val silvilstandsdetaljer = sivilstandsdetaljer()
                .copy(samlivsbruddsdokumentasjon = lagSamlivsbruddsdokumentasjonSøknadsfelt(vedlegg),
                      samlivsbruddsdato = lagSamlivsbruddsdatoSøknadsfelt(sivilstatus))
        return Søknadsfelt("silvilstandsdetaljer", silvilstandsdetaljer)
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
        val felt = dto.filter { vedlegg -> vedlegg.navn.equals("separertEllerSkiltIUtlandet") }.first()
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
