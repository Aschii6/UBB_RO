#!/bin/sh

for file in $@; do
	if [ -f $file ]; then
		wc -c $file
	fi
done | sort -n
