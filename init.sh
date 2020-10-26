export CREDENTIAL_PASSWORD=$(cat /var/run/secrets/srvfam-ef-soknad-api/password)
export CREDENTIAL_USERNAME=$(cat /var/run/secrets/srvfam-ef-soknad-api/username)
echo "- exported CREDENTIAL_USERNAME og CREDENTIAL_PASSWORD for familie-ef-soknad-api "