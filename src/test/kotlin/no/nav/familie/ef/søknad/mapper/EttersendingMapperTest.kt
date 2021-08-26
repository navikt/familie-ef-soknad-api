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

        val forventetMap = listOf("59b846eb-12b8-4321-a36b-29170b81a4d1",
                                  "4f44f53e-5566-4363-9d3c-e30387d4164f",
                                  "6bcc08af-be76-4756-b938-0a7c5934a736").associateWith { it.toByteArray() }
        val ettersendingMap = mapper.mapTilIntern(ettersendignForSøknadDto, innsendingMottatt).vedlegg
        assertEquals(ettersendingMap.keys, forventetMap.keys)
    }

    @Test
    fun `mapTilIntern skal legge alle vedlegg med og uten dokumentasjonsbehov i liste på formatet Vedlegg(id,navn,tittel)`() {

        val vedleggListe: MutableList<Vedlegg> = mutableListOf()
        ettersendignForSøknadDto.ettersendingForSøknad?.dokumentasjonsbehov?.map { dokumenasjonsbehov ->
            dokumenasjonsbehov.opplastedeVedlegg.map {
                vedleggListe.add(Vedlegg(id = it.id, navn = it.navn, tittel = dokumenasjonsbehov.label))
            }
        }
        ettersendignForSøknadDto.ettersendingForSøknad?.innsending?.map { innsending ->
            innsending.vedlegg.map {
                vedleggListe.add(Vedlegg(id = it.id, navn = it.navn, tittel = innsending.beskrivelse))
        }}
        val ettersendingVedlegg = mapper.mapTilIntern(ettersendignForSøknadDto, innsendingMottatt).ettersendingMedVedlegg.vedlegg
        assertEquals(vedleggListe, ettersendingVedlegg)
    }


    @Test
    fun `mapTilIntern returnerer samme datoMottatt og fnr, og stønadType som blir sendt frontend`() {

        val forventetMap = listOf("9f4d99bf-e76b-4725-a602-e6dcc786a848",
                "30de8f93-884b-4c6f-ae78-2ee6568bc82b",
                "30de9f93-884b-4c6f-ae78-2ee6568bc82b").associateWith { it.toByteArray() }

        val innsendtDto = ettersendignUtenSøknadDto
        val (ettersendingMedVedlegg, vedlegg) = mapper.mapTilIntern(innsendtDto, innsendingMottatt)

        assertTrue(innsendingMottatt == ettersendingMedVedlegg.innsendingsdetaljer.verdi.datoMottatt.verdi
                   && innsendtDto.fnr == ettersendingMedVedlegg.ettersending.fnr
                   && innsendtDto.stønadType === ettersendingMedVedlegg.ettersending.stønadType)

        assertEquals(vedlegg.keys, forventetMap.keys)
    }

}