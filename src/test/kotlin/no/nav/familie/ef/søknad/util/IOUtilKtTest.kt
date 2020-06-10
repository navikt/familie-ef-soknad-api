package no.nav.familie.ef.søknad.util

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class IOUtilKtTest{

    @Test
    fun `Like tekster skal ha lik digest `() {
        assertEquals(lagDigest("Dette er en test"), lagDigest("Dette er en test"))
    }

    @Test
    fun `Digest skal ikke være lik input`() {
        assertNotEquals(lagDigest("Dette er en test"), "Dette er en test")
    }

    @Test
    fun `Digest skal ikke være lik når input er forskjellig`() {
        assertNotEquals(lagDigest("Dette er en test"), lagDigest("dette er en test"))
    }

    @Test
    fun `Digest skal ha lengde`() {
        assertNotNull(lagDigest(""))
        assertTrue(lagDigest("").length >0)
    }


}


