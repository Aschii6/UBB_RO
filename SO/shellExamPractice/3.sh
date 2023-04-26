#!/bin/sh

log_files=`find -type f -name "*.log"`

for file in $log_files; do
	cat $file | sort > aux.txt
	cat aux.txt > $file
done
