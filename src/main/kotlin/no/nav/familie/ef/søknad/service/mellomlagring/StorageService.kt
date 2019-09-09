package no.nav.familie.ef.søknad.service.mellomlagring

import no.nav.familie.ef.søknad.excpetion.AttachmentVirusException
import no.nav.familie.ef.søknad.service.VirusScanner
import no.nav.familie.ef.søknad.util.TokenUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StorageService(private val tokenHelper: TokenUtil,
                     private val storage: Storage,
                     private val virusScanner: VirusScanner,
                     private val crypto: StorageCrypto) {

    fun hentSøknad(): String? {
        val fnr = tokenHelper.fødselsnummer
        val directory = crypto.encryptDirectoryName(fnr)
        LOG.trace("Henter søknad fra katalog {}", directory)
        return storage.getTmp(directory, SØKNAD)?.let { crypto.decrypt(it, fnr) }
    }

    fun lagreSøknad(søknad: String) {
        val fnr = tokenHelper.fødselsnummer
        val directory = crypto.encryptDirectoryName(fnr)
        LOG.trace("Skriver søknad til katalog {}", directory)
        val encryptedValue = crypto.encrypt(søknad, fnr)
        storage.putTmp(directory, SØKNAD, encryptedValue)
    }

    fun slettSøknad() {
        val fnr = tokenHelper.fødselsnummer
        val directory = crypto.encryptDirectoryName(fnr)
        LOG.info("Fjerner søknad fra katalog {}", directory)
        storage.deleteTmp(directory, SØKNAD)
    }

    fun hentVedlegg(key: String): Vedlegg? {
        val fnr = tokenHelper.fødselsnummer
        val directory = crypto.encryptDirectoryName(fnr)
        LOG.info("Henter vedlegg med nøkkel {} fra katalog {}", key, directory)
        return storage.getTmp(directory, key)?.let { Vedlegg.fromJson(crypto.decrypt(it, fnr)) }
    }

    fun lagreVedlegg(vedlegg: Vedlegg) {
        if (!virusScanner.scan(vedlegg)) {
            throw AttachmentVirusException(vedlegg)
        }
        val fnr = tokenHelper.fødselsnummer
        val directory = crypto.encryptDirectoryName(fnr)
        LOG.info("Skriver vedlegg {} til katalog {}", vedlegg, directory)
        val encryptedValue = crypto.encrypt(vedlegg.toJson(), fnr)
        storage.putTmp(directory, vedlegg.uuid, encryptedValue)
    }

    fun slettVedlegg(key: String) {
        val directory = crypto.encryptDirectoryName(tokenHelper.fødselsnummer)
        LOG.info("Fjerner vedlegg med nøkkel {} fra katalog {}", key, directory)
        storage.deleteTmp(directory, key)
    }

    fun hentKvittering(type: String): String? {
        val fnr = tokenHelper.fødselsnummer
        val directory = crypto.encryptDirectoryName(fnr)
        LOG.trace("Henter kvittering fra katalog {}", directory)

        return  storage.get(directory, type)?.let { crypto.decrypt(it, fnr) }
    }


    fun lagreKvittering(type: String, kvittering: String) {
        val fnr = tokenHelper.fødselsnummer
        val directory = crypto.encryptDirectoryName(fnr)
        LOG.trace("Skriver kvittering til katalog {}", directory)
        val encryptedValue = crypto.encrypt(kvittering, fnr)
        storage.put(directory, type, encryptedValue)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(StorageService::class.java)

        private const val SØKNAD = "soknad"
    }
}
