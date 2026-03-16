package no.nav.familie.ef.søknad.infrastruktur.config

import tools.jackson.core.type.TypeReference
import tools.jackson.databind.json.JsonMapper
import java.io.File
import java.io.InputStream
import java.net.URL

/**
 * Extension functions to provide reified type parameter for readValue in Jackson 3
 */
inline fun <reified T> JsonMapper.readValue(content: String): T = readValue(content, object : TypeReference<T>() {})

inline fun <reified T> JsonMapper.readValue(content: ByteArray): T = readValue(content, object : TypeReference<T>() {})

inline fun <reified T> JsonMapper.readValue(content: URL): T = readValue(content.openStream(), object : TypeReference<T>() {})

inline fun <reified T> JsonMapper.readValue(content: File): T = readValue(content, object : TypeReference<T>() {})

inline fun <reified T> JsonMapper.readValue(content: InputStream): T = readValue(content, object : TypeReference<T>() {})
