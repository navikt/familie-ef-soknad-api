package no.nav.familie.ef.søknad.mellomlagring

interface Storage {

    fun put(directory: String, key: String, value: String)

    fun putTmp(directory: String, key: String, value: String)

    fun get(directory: String, key: String): String?

    fun getTmp(directory: String, key: String): String?

    fun delete(directory: String, key: String)

    fun deleteTmp(directory: String, key: String)
}
