package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.mapper.kontrakt.EttersendingMapper
import no.nav.familie.ef.søknad.mock.DokumentServiceStub
import org.junit.jupiter.api.Test
import no.nav.familie.ef.søknad.mock.ettersendingUtenSøknadDto
import no.nav.familie.ef.søknad.mock.ettersendingForSøknadDto
import no.nav.familie.kontrakter.ef.søknad.Vedlegg
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions.assertTrue

internal class EttersendingMapperTest {

    private val mapper = EttersendingMapper(DokumentServiceStub())
    private val ettersendignUtenSøknadDto = ettersendingUtenSøknadDto()
    private val ettersendignForSøknadDto = ettersendingForSøknadDto()
    private val innsendingMottatt: LocalDateTime = LocalDateTime.now()

    @Test
    fun `mapTilIntern setter sammen en liste med Map(String to ByteArray) fra alle innsendte vedlegg med id som nøkkel`() {

        //Given
        val forventetMap = listOf("59b846eb-12b8-4321-a36b-29170b81a4d1",
                                  "4f44f53e-5566-4363-9d3c-e30387d4164f",
                                  "6bcc08af-be76-4756-b938-0a7c5934a736").associateWith { it.toByteArray() }
        //When
        val ettersendingMap = mapper.mapTilIntern(ettersendignForSøknadDto, innsendingMottatt).vedlegg
        //Then
        assertEquals(ettersendingMap.keys, forventetMap.keys)
    }

    @Test
    fun `mapTilIntern skal legge alle vedlegg med og uten dokumentasjonsbehov i liste på formatet Vedlegg(id,navn,tittel)`() {

        //Given
        val vedleggListe: MutableList<Vedlegg> = mutableListOf()
        ettersendignForSøknadDto.ettersendingForSøknad?.dokumentasjonsbehov?.map { dokumenasjonsbehov ->
            dokumenasjonsbehov.opplastedeVedlegg.map {
                vedleggListe.add(Vedlegg(id = it.id, navn = it.navn, tittel = dokumenasjonsbehov.label))
            }
        }
        ettersendignForSøknadDto.ettersendingForSøknad?.innsending?.map {
            vedleggListe.add(Vedlegg(id = it.vedlegg.id, navn = it.vedlegg.navn, tittel = it.beskrivelse))
        }
        //When
        val ettersendingVedlegg = mapper.mapTilIntern(ettersendignForSøknadDto, innsendingMottatt).ettersendingMedVedlegg.vedlegg
        //Then
        assertEquals(vedleggListe, ettersendingVedlegg)
    }


    @Test
    fun `mapTilIntern returnerer samme datoMottatt og fnr som blir sendt frontend`() {

        //Given
        val innsendtDto = ettersendignUtenSøknadDto
        //When
        val mapper = mapper.mapTilIntern(innsendtDto, innsendingMottatt).ettersendingMedVedlegg
        //Then
        assertTrue(innsendingMottatt == mapper.innsendingsdetaljer.verdi.datoMottatt.verdi && innsendtDto.fnr == mapper.ettersending.fnr)
    }

}