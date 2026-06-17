package no.nav.familie.ef.søknad.kodeverk

import java.text.Collator
import java.util.Locale

data class Landkode(
    val kode: String,
    val navn: String,
    val erEøsland: Boolean,
)

enum class Språk(
    val tag: String,
) {
    NB("nb"),
    NN("nn"),
    EN("en"),
    ;

    val locale: Locale = Locale.forLanguageTag(tag)

    fun collator(): Collator = Collator.getInstance(locale)

    companion object {
        fun fra(verdi: String): Språk =
            entries.firstOrNull { it.tag.equals(other = verdi, ignoreCase = true) }
                ?: throw IllegalArgumentException(
                    "Ukjent språk: '$verdi'. Forventet en av: ${entries.joinToString { it.tag }}",
                )
    }
}

val EOS_LAND: Set<String> =
    setOf(
        "AUT",
        "BEL",
        "BGR",
        "HRV",
        "CYP",
        "CZE",
        "DNK",
        "EST",
        "FIN",
        "FRA",
        "DEU",
        "GRC",
        "HUN",
        "IRL",
        "ISL",
        "ITA",
        "LVA",
        "LIE",
        "LTU",
        "LUX",
        "MLT",
        "NLD",
        "NOR",
        "POL",
        "PRT",
        "ROU",
        "SVK",
        "SVN",
        "ESP",
        "SWE",
    )

private val alpha3TilAlpha2: Map<String, String> by lazy {
    Locale
        .getISOCountries()
        .mapNotNull { alpha2 ->
            val alpha3 = Locale.of("", alpha2).isO3Country
            if (alpha3.isNotBlank()) alpha3 to alpha2 else null
        }.toMap()
}

fun lokalisertLandnavn(
    alpha3: String,
    språk: Språk,
): String? {
    val alpha2 = alpha3TilAlpha2[alpha3] ?: return null
    val displayName = Locale.of("", alpha2).getDisplayCountry(språk.locale)
    return displayName.takeIf { it.isNotBlank() && it != alpha2 }
}

fun tilTitlecase(
    input: String,
    locale: Locale,
): String {
    if (input.isBlank()) return input

    val resultat = StringBuilder(input.length)
    var nesteErStor = true

    for (c in input) {
        if (c == ' ' || c == '-') {
            resultat.append(c)
            nesteErStor = true
        } else {
            resultat.append(if (nesteErStor) c.toString().uppercase(locale) else c.toString().lowercase(locale))
            nesteErStor = false
        }
    }

    return resultat.toString()
}

fun fallbackLandliste(språk: Språk): List<Landkode> =
    alpha3TilAlpha2.keys
        .mapNotNull { alpha3 ->
            val navn = lokalisertLandnavn(alpha3 = alpha3, språk = språk) ?: return@mapNotNull null
            Landkode(kode = alpha3, navn = navn, erEøsland = alpha3 in EOS_LAND)
        }.sortedWith(compareBy(språk.collator()) { it.navn })
