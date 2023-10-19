#!/bin/sh

DIR="dir"

files=`find $DIR -type f`

for file in $files; do
	if [ -w $file ]; then
		echo `ls -l $file | awk '{print $1, $9}'`
		chmod a-w $file
		echo `ls -l $file | awk '{print $1, $9}'`
	fi
done
