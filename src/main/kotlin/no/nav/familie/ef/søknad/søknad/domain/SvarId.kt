package no.nav.familie.ef.søknad.søknad.domain

enum class SvarId(
    val verdi: String,
) {
    JA("ja"),
    NEI("nei"),

    // val årsakEnslig: TekstFelt? = null,
    SAMLIVSBRUDD_FOREDLRE("samlivsbruddForeldre"),
    SAMLIVSBRUDD_ANDRE("samlivsbruddAndre"),
    ALENE_FRA_FØDSEL("aleneFraFødsel"),
    ENDRING_I_SAMVÆRSORDNING("endringISamværsordning"),
    DØDSFALL("dødsfall"),

    // Skriftlig samværsavtale
    JA_KONKRETE_TIDSPUNKTER("jaKonkreteTidspunkter"),
    JA_IKKE_KONKRETE_TIDSPUNKTER("jaIkkeKonkreteTidspunkter"),

    // Kontakt mellom foreldre
    MØTES_IKKE("møtesIkke"),
    KUN_NÅR_LEVERES("kunNårLeveres"),
    MØTES_UTENOM("møtesUtenom"),

    // Medlemskap - Oppholdsland

    // Bosituasjon - delerDuBolig = bosituasjon.delerDuBolig.svarId,
    BOR_ALENE_MED_BARN_ELLER_GRAVID("borAleneMedBarnEllerGravid"),
    BOR_MIDLERTIDIG_FRA_HVERANDRE("borMidlertidigFraHverandre"),
    BOR_SAMMEN_OG_VENTER_BARN("borSammenOgVenterBarn"),
    HAR_EKTESKAPSLIKNENDE_FORHOLD("harEkteskapsliknendeForhold"),
    DELER_BOLIG_MED_ANDRE_VOKSNE("delerBoligMedAndreVoksne"),
    TIDLIGERE_SAMBOER_FORTSATT_REGISTRERT_PÅ_ADRESSE("tidligereSamboerFortsattRegistrertPåAdresse"),

    // Aktivitet
    // hvordanErArbeidssituasjonen
    // arbeidsgiver - fastEllerMidlertidig
    // arbeidsforhold = Arbeidsgiver fast eller midlertidig
    // under utdanning - offentligEllerPrivat, heltidEllerDeltid
    PRIVAT("privat"),
    OFFENTLIG("offentlig"),
    ;

    companion object {
        private val map = entries.associateBy { it.verdi }

        fun fromVerdi(verdi: String): SvarId? = map[verdi]
    }
}
