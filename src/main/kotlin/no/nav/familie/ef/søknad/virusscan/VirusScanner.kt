package no.nav.familie.ef.søknad.virusscan

import no.nav.familie.ef.søknad.mellomlagring.Vedlegg

interface VirusScanner {
    fun scan(vedlegg: Vedlegg): Boolean
}
