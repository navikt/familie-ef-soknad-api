# familie-ef-soknad-api

## Kjør på localhost: 
bruk Launcher : ApplicationLocalLauncher 
Vil kjøre med tps-innsyn mock TpsInnsynMockController

## Hvordan logge inn på localhost:
Eksempel: 
http://localhost:8091/local/cookie?redirect=/api/ping
(redirect kan fjernes hvis man ønsker å se token i browser)

### Swagger api kall med autentisering: 
http://localhost:8091/swagger-ui.html
På localhost skal det være nok å logge inn som beskrevet ovenfor. I dev må man lime inn "Bearer + token" i Authorize oppe til høre i swagger-ui.html. Dersom Bearer limes inn på denne måten må Cookie med Authorization slettes (localhost).   