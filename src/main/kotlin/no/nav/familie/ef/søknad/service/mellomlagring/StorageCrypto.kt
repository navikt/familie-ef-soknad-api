package no.nav.familie.ef.søknad.service.mellomlagring

import no.nav.familie.ef.søknad.config.S3StorageConfiguration
import org.springframework.stereotype.Component

import javax.xml.bind.DatatypeConverter.printHexBinary

@Component
class StorageCrypto(private val configuration: S3StorageConfiguration) {

    fun encryptDirectoryName(plaintext: String): String {
        return printHexBinary(encrypt(plaintext, plaintext).toByteArray())
    }

    fun encrypt(plaintext: String, fnr: String): String {
        return Crypto(configuration.passordfrase, fnr).encrypt(plaintext)
    }

    fun decrypt(encrypted: String, fnr: String): String {
        return Crypto(configuration.passordfrase, fnr).decrypt(encrypted)
    }
}
