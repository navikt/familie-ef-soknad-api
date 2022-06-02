package no.nav.familie.ef.søknad.mock

import no.nav.familie.kontrakter.ef.ettersending.EttersendingDto
import no.nav.familie.kontrakter.felles.objectMapper
import java.io.File

fun ettersendingForSøknadDto(): EttersendingDto = objectMapper.readValue(File("src/test/resources/ettersendingForSøknadDto.json"), EttersendingDto::class.java)

fun ettersendingUtenSøknadDto(): EttersendingDto = objectMapper.readValue(File("src/test/resources/ettersendingUtenSøknadDto.json"), EttersendingDto::class.java)
