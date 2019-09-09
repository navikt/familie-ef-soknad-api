package no.nav.familie.ef.soknad.featuretoggle

interface UnleashService {
    fun isEnabled(toggleId:String) : Boolean
    fun isEnabled(toggleId:String, defaultValue:Boolean) : Boolean
}