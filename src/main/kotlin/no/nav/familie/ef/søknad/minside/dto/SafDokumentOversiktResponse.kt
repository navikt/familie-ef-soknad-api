package no.nav.familie.ef.søknad.minside.dto

data class SafDokumentOversiktResponse<T>(
    val data: T,
    val errors: List<SafError>? = null,
) {
    fun harFeil(): Boolean {
        return !errors.isNullOrEmpty()
    }

    fun errorMessages(): String {
        return errors?.joinToString { it.message } ?: ""
    }
}

data class SafError(
    val message: String,
    val extensions: SafExtension,
)

data class SafExtension(
    val code: SafErrorCode,
    val classification: String,
)

@Suppress("EnumEntryName")
enum class SafErrorCode {
    forbidden,
    not_found,
    bad_request,
    server_error,
}
