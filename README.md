# familie-ef-soknad-api
Denne applikasjonen tilbyr apier for Team Enslig Forsørger sine eksterne flater:
* [søknad om overgansstønad](https://www.nav.no/familie/alene-med-barn/soknad/)
* [søknad om barnetilsyn](https://www.nav.no/familie/alene-med-barn/soknad/barnetilsyn)
* [søknad om skolepenger](https://www.nav.no/familie/alene-med-barn/soknad/skolepenger)
* [ettersending av dokumentasjon](https://www.nav.no/familie/alene-med-barn/ettersending)
* [min side](https://www.nav.no/familie/alene-med-barn/minside)

## Kjør på localhost: 
bruk Launcher : ApplicationLocalLauncher 

### Hvordan logge inn på localhost:
Eksempel:
http://localhost:8091/local/cookie?issuerId=tokenx&audience=familie-app&redirect=
Med redirect:
http://localhost:8091/local/cookie?redirect=http://localhost:3000/familie/alene-med-barn/soknad&issuerId=tokenx&audience=familie-app&redirect=

PS. Mapping av fødselsnummer til kontrakt krever gyldig fødselsnummer: Logg f.eks. inn med et fnr fra dolly.
http://localhost:8091/local/cookie?subject=[gyldigFødselsnummer] dersom du skal teste noe som krever at mapping skal fungere.   

### Swagger api kall med autentisering: 
http://localhost:8091/swagger-ui.html
https://familie-ef-soknad-api.intern.dev.nav.no/swagger-ui.html

## Henvendelser for Nav-ansatte
Interne henvendelser kan sendes via Slack i kanalen `#team-familie`.

## Kode generert av GitHub Copilot
Dette repoet bruker GitHub Copilot til å generere kode.