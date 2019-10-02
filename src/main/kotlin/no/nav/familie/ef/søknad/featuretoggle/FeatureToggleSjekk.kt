package no.nav.familie.ef.søknad.featuretoggle

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException


fun <T> FeatureToggleService.enabledEllersHttp404(toggleId: String, eksekver: () -> T): T {
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

@ResponseStatus(code = HttpStatus.NOT_FOUND)
private class FeatureToggleFeilTilstandException(toggle: String) : RuntimeException(toggle)