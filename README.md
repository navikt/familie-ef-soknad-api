# familie-ef-soknad-api

## Kjør på localhost: 
bruk Launcher : ApplicationLocalLauncher 

## Hvordan logge inn på localhost:
Eksempel:
http://localhost:8091/local/cookie?issuerId=selvbetjening&audience=aud-localhost
Med redirect:
http://localhost:8091/local/cookie?redirect=http://localhost:3000/familie/alene-med-barn/soknad&issuerId=selvbetjening&audience=aud-localhost

PS. Mapping av fødselsnummer til kontrakt krever gyldig fødselsnummer: Logg f.eks. inn med et fnr fra dolly.
http://localhost:8091/local/cookie?subject=[gyldigFødselsnummer] dersom du skal teste noe som krever at mapping skal fungere.   

### Swagger api kall med autentisering: 
http://localhost:8091/swagger-ui.html
https://familie.dev.nav.no/familie/alene-med-barn/soknad-api/swagger-ui.html
