package no.nav.familie.ef.søknad.util

import java.io.IOException
import java.net.URISyntaxException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

@Throws(IOException::class, URISyntaxException::class)

fun getFileAsString(filePath: String) = String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8)