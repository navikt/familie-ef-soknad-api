package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Dokumentasjonsbehov
import no.nav.familie.ef.søknad.mapper.kontrakt.EttersendingMapper
import no.nav.familie.ef.søknad.mock.DokumentServiceStub
import org.junit.jupiter.api.Test
import no.nav.familie.ef.søknad.mock.ettersendingUtenSøknadDto
import no.nav.familie.ef.søknad.mock.ettersendingForSøknadDto
import no.nav.familie.kontrakter.ef.søknad.Vedlegg
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions.assertTrue

internal class EttersendingMapperTest {

    private val mapper = EttersendingMapper(DokumentServiceStub())
    private val ettersendignUtenSøknadDto = ettersendingUtenSøknadDto()
    private val ettersendignForSøknadDto = ettersendingForSøknadDto()
    private val innsendingMottatt: LocalDateTime = LocalDateTime.now()

    @Test
    fun `mapTilIntern setter sammen en liste på formatet Map(String to ByteArray) fra alle innsendte vedlegg`() {

        //Given
        val vedleggIderFraDto = listOf("98", "123", "122")
        val forventetMap = vedleggIderFraDto.associateWith { it.toByteArray() }

        //When
        val ettersendingMap = mapper.mapTilIntern(ettersendignForSøknadDto, innsendingMottatt).vedlegg

        //Then
        assertTrue(ettersendingMap.keys.equals(forventetMap.keys))
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
        assertTrue(vedleggListe.equals(ettersendingVedlegg))
    }

    @Test
    fun `mapTilIntern skal fungere med å ikke sende inn innsendingForSøknad og dokumentasjonsbehov skal da bli emptyList()`() {

        //Given
        val tomDokumentasjonsbehovListe: List<Dokumentasjonsbehov> = emptyList()
        //When
        val ettersendingmapper = mapper.mapTilIntern(ettersendignUtenSøknadDto, innsendingMottatt)
        //Then
        assertTrue(tomDokumentasjonsbehovListe.equals(ettersendingmapper.ettersendingMedVedlegg.dokumentasjonsbehov))
    }

    @Test
    fun `mapTilIntern returnerer samme samme datoMottatt og fnr som blir sendt frontend`() {

        //Given
        val innsendtDto = ettersendignUtenSøknadDto
        //When
        val mapper = mapper.mapTilIntern(innsendtDto, innsendingMottatt).ettersendingMedVedlegg.ettersending
        //Then
        assertTrue(innsendingMottatt.equals(mapper.innsendingsdetaljer.verdi.datoMottatt.verdi) && innsendtDto.fnr.equals(mapper.fnr))

    }

}