# familie-ef-soknad-api

## Kjør på localhost: 
bruk Launcher : ApplicationLocalLauncher 

## Hvordan logge inn på localhost:
Eksempel:
http://localhost:8091/local/cookie?issuerId=tokenx&audience=familie-app&redirect=
Med redirect:
http://localhost:8091/local/cookie?redirect=http://localhost:3000/familie/alene-med-barn/soknad&issuerId=tokenx&audience=familie-app&redirect=

PS. Mapping av fødselsnummer til kontrakt krever gyldig fødselsnummer: Logg f.eks. inn med et fnr fra dolly.
http://localhost:8091/local/cookie?subject=[gyldigFødselsnummer] dersom du skal teste noe som krever at mapping skal fungere.   

### Swagger api kall med autentisering: 
http://localhost:8091/swagger-ui.html
https://familie-ef-soknad-api.intern.dev.nav.no/swagger-ui.html

## Kode generert av GitHub Copilot

Dette repoet bruker GitHub Copilot til å generere kode.