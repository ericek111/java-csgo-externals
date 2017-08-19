#!/bin/bash
if [ "$(whoami)" != 'root' ]; then
	echo "ERROR: You *must* run this script as root!"
	exit 1;
fi
ABSPATH=`pwd`
java -cp "$ABSPATH/lib/*:$ABSPATH/CSGOExternals.jar" me.lixko.csgoexternals.Main "$@"
