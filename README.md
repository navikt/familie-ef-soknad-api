# familie-ef-soknad-api

## Kjør på localhost: 
bruk Launcher : ApplicationLocalLauncher 
Vil kjøre med tps-innsyn mock TpsInnsynMockController (som bla.a muliggjør innlogging - se under)

## Hvordan logge inn på localhost:
Eksempel: 
http://localhost:8091/local/cookie
With redirect:
http://localhost:8091/local/cookie?redirect=/api/ping

PS. Mapping av fødselsnummer til kontrakt krever gyldig fødselsnummer: Logg inn med eks 21057822284 (brukes i koden)
http://localhost:8091/local/cookie?subject=[gyldigFødselsnummer] dersom du skal teste noe som krever at mapping skal fungere.   

### Swagger api kall med autentisering: 
http://localhost:8091/swagger-ui.html
