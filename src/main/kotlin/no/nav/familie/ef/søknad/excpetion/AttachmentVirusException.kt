package no.nav.familie.ef.søknad.excpetion

import no.nav.familie.ef.søknad.service.mellomlagring.Vedlegg

class AttachmentVirusException(val vedlegg: Vedlegg) : AttachmentException(msg = vedlegg.filename)
