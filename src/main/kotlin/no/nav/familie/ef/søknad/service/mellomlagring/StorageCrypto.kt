package no.nav.familie.ef.søknad.service.mellomlagring

import no.nav.familie.ef.søknad.config.S3StorageConfiguration
import org.springframework.stereotype.Component

import javax.xml.bind.DatatypeConverter.printHexBinary

@Component
class StorageCrypto(configuration: S3StorageConfiguration) {

    private val encryptionPassphrase = configuration.passphrase

    fun encryptDirectoryName(plaintext: String): String {
        return printHexBinary(encrypt(plaintext, plaintext).toByteArray())
    }

    fun encrypt(plaintext: String, fnr: String): String {
        return Crypto(encryptionPassphrase, fnr).encrypt(plaintext)
    }

    fun decrypt(encrypted: String, fnr: String): String {
        return Crypto(encryptionPassphrase, fnr).decrypt(encrypted)
    }
}
