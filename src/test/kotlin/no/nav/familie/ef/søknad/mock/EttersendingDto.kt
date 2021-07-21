package no.nav.familie.ef.søknad.mock

import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.familie.ef.søknad.api.dto.ettersending.EttersendingDto
import no.nav.familie.kontrakter.felles.objectMapper
import java.io.File


fun EttersendingForSøknadDto(): EttersendingDto = objectMapper.readValue(File("src/test/resources/ettersendingDtoForSøknad.json"), EttersendingDto::class.java)

fun EttersendingUtenSøknadDto(): EttersendingDto = objectMapper.readValue(File("src/test/resources/ettersendingDtoUtenSøknad.json"), EttersendingDto::class.java)