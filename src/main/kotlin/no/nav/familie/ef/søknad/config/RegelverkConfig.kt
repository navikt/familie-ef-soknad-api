package no.nav.familie.ef.søknad.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class RegelverkConfig(@Value("\${familie.ef.soknad.regelverk.maks.alder.barn}") val maksAlderIÅrBarn: Int)
