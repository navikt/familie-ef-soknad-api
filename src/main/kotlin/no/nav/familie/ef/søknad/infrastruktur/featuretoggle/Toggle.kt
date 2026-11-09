package no.nav.familie.ef.søknad.infrastruktur.featuretoggle

enum class Toggle(
    val toggleId: String,
    val beskrivelse: String? = null,
) {
    NYNORSK("familie.ef.soknad.nynorsk"),
    GJENBRUK_BARNETILSYN("familie.ef.soknad.gjenbruk-barnetilsyn"),
    OVERGANGSSTØNAD_REGELENDRINGER_2026("familie.ef.soknad.overgangsstonad-regelendringer-2026"),
    ;

    companion object {
        private val toggles: Map<String, Toggle> = values().associateBy { it.name }

        fun byToggleId(toggleId: String): Toggle = toggles[toggleId] ?: error("Finner ikke toggle for $toggleId")
    }
}
