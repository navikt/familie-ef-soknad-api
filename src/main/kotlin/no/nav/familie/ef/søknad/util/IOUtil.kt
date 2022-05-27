package no.nav.familie.ef.søknad.util

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

fun getFileAsString(filePath: String) = String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8)
