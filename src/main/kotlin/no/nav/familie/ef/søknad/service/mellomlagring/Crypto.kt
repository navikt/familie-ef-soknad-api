package no.nav.familie.ef.søknad.service.mellomlagring

import org.apache.logging.log4j.util.Strings
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class Crypto(passphrase: String, private val iv: String) {

    private val key: SecretKey

    init {
        require(!(Strings.isBlank(passphrase) || Strings.isBlank(iv))) { "Both passphrase and fnr must be provided" }
        key = key(passphrase, iv)
    }

    fun encrypt(plainText: String): String {
        try {
            val cipher = Cipher.getInstance(ALGORITME)
            cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(128, iv.toByteArray()))
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.toByteArray()))
        } catch (ex: Exception) {
            throw RuntimeException("Error while encrypting text", ex)
        }

    }

    fun decrypt(encrypted: String): String {
        try {
            val cipher = Cipher.getInstance(ALGORITME)
            cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv.toByteArray()))
            return String(cipher.doFinal(Base64.getDecoder().decode(encrypted)))
        } catch (ex: Exception) {
            throw RuntimeException("Error while decrypting text", ex)
        }

    }

    private fun key(passphrase: String, salt: String): SecretKey {
        try {
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val passwordChars = passphrase.toCharArray()
            val spec = PBEKeySpec(passwordChars, salt.toByteArray(), 10000, 256)
            val key = factory.generateSecret(spec)
            return SecretKeySpec(key.encoded, "AES")
        } catch (ex: Exception) {
            throw RuntimeException("Error while generating key", ex)
        }

    }

    companion object {

        private const val ALGORITME = "AES/GCM/NoPadding"
    }

}
