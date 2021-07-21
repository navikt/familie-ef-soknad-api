package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.mapper.kontrakt.EttersendingMapper
import no.nav.familie.ef.søknad.mock.DokumentServiceStub
import org.junit.jupiter.api.Test
import no.nav.familie.ef.søknad.mock.ettersendingUtenSøknadDto
import no.nav.familie.ef.søknad.mock.ettersendingForSøknadDto
import no.nav.familie.kontrakter.ef.søknad.Vedlegg
import java.time.LocalDateTime


internal class EttersendingMapperTest {


    private val mapper = EttersendingMapper(DokumentServiceStub())

    private val ettersendignUtenSøknadDto = ettersendingUtenSøknadDto()
    private val ettersendignForSøknadDto = ettersendingForSøknadDto()

    private val innsendingMottatt: LocalDateTime = LocalDateTime.now()


    @Test
    fun `map test`() {
        //When
        val vedleggListe = ettersendignForSøknadDto.ettersendingForSøknad?.dokumentasjonsbehov?.map { dokumentasjonsbehov ->
            dokumentasjonsbehov.opplastedeVedlegg.map {
                Vedlegg(
                        it.id,
                        it.navn,
                        dokumentasjonsbehov.label
                )
            }
        }



    }

}