package no.nav.familie.ef.søknad.infrastruktur.featuretoggle

enum class Toggle(
    val toggleId: String,
    val beskrivelse: String? = null,
) {
    NYNORSK("familie.ef.soknad.nynorsk"),
    GJENBRUK_BARNETILSYN("familie.ef.soknad.gjenbruk-barnetilsyn"),
    OS_REGELENDRINGER("familie.ef.soknad.nye-regler-overgangsstonad"),
    ;

    companion object {
        private val toggles: Map<String, Toggle> = values().associateBy { it.name }

        fun byToggleId(toggleId: String): Toggle = toggles[toggleId] ?: error("Finner ikke toggle for $toggleId")
    }
}
