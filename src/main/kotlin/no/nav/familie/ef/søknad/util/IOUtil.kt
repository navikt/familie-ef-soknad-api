package no.nav.familie.ef.søknad.util

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.security.MessageDigest

fun getFileAsString(filePath: String) = String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8)

fun lagDigest(value: Any) = MessageDigest
        .getInstance("SHA-1")
        .digest(value.toString().toByteArray())
        .fold("", { str, it -> str + "%02x".format(it) })

