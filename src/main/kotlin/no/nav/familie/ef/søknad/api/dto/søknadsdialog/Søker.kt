package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import no.nav.familie.ef.søknad.validering.SjekkGyldigFødselsnummer

data class Søker(val adresse: Adresse,
                 val egenansatt: Boolean,
                 @field:SjekkGyldigFødselsnummer
                 val fnr: String,
                 val forkortetNavn: String,
                 val sivilstand: String,
                 val statsborgerskap: String,
                 val kontakttelefon: String? = null)

// TODO Ikke fra UI
// telefonnummer?
// Mangler i kontrakt?
// val telefonnummer: String?,
//                 val innvandretDato: String?,
//                 val utvandretDato: String?,
//                 val oppholdstillatelse: String?,
//                 val språk: String?,
//                 val språk: String?,
// Neppe interessant:
//    privattelefon?: string;
//    mobiltelefon?: string;
//    jobbtelefon?: string;
//    bankkontonummer?: string;


//export interface IPerson {
//    søker: ISøker;
//    barn: IBarn[];
//}
//
//export interface ISøker {
//    fnr: string; x
//    forkortetNavn: string; x
//    adresse: IAdresse; x
//    egenansatt: boolean; x
//    innvandretDato?: string; x
//    utvandretDato?: string;x
//    oppholdstillatelse?: string;x
//    sivilstand: string; x
//    språk: string;x
//    statsborgerskap: string; x
//    privattelefon?: string;
//    mobiltelefon?: string;
//    jobbtelefon?: string;
//    bankkontonummer?: string;
//}
//
//export interface IAdresse {
//    adresse: string;
//    adressetillegg: string;
//    kommune: string;
//    postnummer: string;
//}
//
//export interface IPersonDetaljer {
//    navn?: string;
//    fødselsdato?: Date;
//    fødselsnummer?: string;
//}