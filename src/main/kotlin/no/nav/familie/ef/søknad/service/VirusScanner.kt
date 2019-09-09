package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.service.mellomlagring.Vedlegg

interface VirusScanner {
    fun scan(vedlegg: Vedlegg): Boolean
}
