package no.nav.familie.ef.søknad.kodeverk

import java.text.Collator
import java.util.Locale

data class Landkode(
    val kode: String,
    val navn: String,
    val erEøsland: Boolean,
)

enum class Spraak(
    val tag: String,
) {
    NB("nb"),
    NN("nn"),
    EN("en"),
    ;

    val locale: Locale get() = Locale.forLanguageTag(tag)
    val collator: Collator get() = Collator.getInstance(locale)

    companion object {
        fun fra(verdi: String): Spraak =
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
            val alpha3 = Locale("", alpha2).isO3Country
            if (alpha3.isNotBlank()) alpha3 to alpha2 else null
        }.toMap()
}

fun lokalisertLandnavn(
    alpha3: String,
    spraak: Spraak,
): String? {
    val alpha2 = alpha3TilAlpha2[alpha3] ?: return null
    val displayName = Locale("", alpha2).getDisplayCountry(spraak.locale)
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

fun fallbackLandliste(spraak: Spraak): List<Landkode> =
    alpha3TilAlpha2.keys
        .mapNotNull { alpha3 ->
            val navn = lokalisertLandnavn(alpha3 = alpha3, spraak = spraak) ?: return@mapNotNull null
            Landkode(kode = alpha3, navn = navn, erEøsland = alpha3 in EOS_LAND)
        }.sortedWith(compareBy(spraak.collator) { it.navn })
