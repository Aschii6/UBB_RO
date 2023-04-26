#!/bin/sh

#nu merge

files=`find dir -type f`

for file1 in $files; do
	for file2 in $files; do
		if [ $file1 != $file2 ] && [ -f $file1 ] && [ -f $file2 ]; then
			echo alt
			if [ `cksum $file1` -eq `cksum $file2` ];then
				echo ceva
			fi
		fi
	done
done
