package no.nav.familie.ef.søknad.featuretoggle

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


fun <T> FeatureToggleService.enabledEllersHttp403(toggleId: String, eksekver: () -> T): T {
    if (this.isEnabled(toggleId))
        return eksekver()
    else
        throw FeatureToggleFeilTilstandException(toggleId)
}

fun <T> FeatureToggleService.disabledEllersHttp404(toggleId: String, eksekver: () -> T): T {
    if (!this.isEnabled(toggleId))
        return eksekver()
    else
        throw FeatureToggleFeilTilstandException(toggleId)
}

@ResponseStatus(code = HttpStatus.FORBIDDEN)
private class FeatureToggleFeilTilstandException(toggle: String)
    : RuntimeException("(Mangel på) funksjonsbryter stopper forespørselen: $toggle")