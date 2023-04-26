#!/bin/sh

c_files=`find dir -type f -name "*.c"`

nr=0

for file in $c_files; do
	nr_lines=`wc -l $file | awk '{print $1}'`
	if [ $nr_lines -ge 500 ]; then
		echo $file
		nr=$((nr+1))
		if [ $nr -ge 2 ]; then
			break
		fi
	fi
done
