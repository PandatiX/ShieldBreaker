#!/bin/bash

# Do not use on a real instance ; for educational purpose only

# Init main variables
SAVE_DIR="logs/"
BASE_URL="clarolineconnect.univ-lyon1.fr/log/view_details/"
INF=1
SUP=1
purge=false

# Parse arguments
for i in "$@"
do
case $i in
	-d=*|--directory=*)
	SAVE_DIR="${i#*=}"
	shift
	;;
	-b=*|--baseurl=*)
	BASE_URL="${i#*=}"
	shift
	;;
	-i=*|--inf=*)
	INF="${i#*=}"
	shift
	;;
	-s=*|--sup=*)
	SUP="${i#*=}"
	shift
	;;
	-p|--purgelogs)
	purge=true
	shift
	;;
esac
done

# Start exec
echo "##### STARTING #####"
echo "SAVE_DIR = ${SAVE_DIR}"
echo "BASE_URL = ${BASE_URL}"
echo "INF      = ${INF}"
echo "SUP      = ${SUP}"
echo ""

# Setting up directory
if [ "$purge" = true ] ; then
	rm -r $SAVE_DIR
fi
mkdir -p $SAVE_DIR

# Extract pages
for i in $(seq $INF $SUP)
do
  currentURL="${BASE_URL}${i}"
	echo "Working on: ${currentURL}"
  content=$(curl -Ls $currentURL)
	# Save content
  echo $content > "${SAVE_DIR}log_${i}.txt"
done
echo ""

# End exec
echo "##### FINISHED #####"
