#!/bin/sh

if [ $# -eq 0 ]; then
	cat ps.fake | awk '{print $1}' | sort | uniq -c
else
	rez=""
	for p in $@; do
		procese=`cat ps.fake | awk -v user=$p '{if($1==user)print $1}'`
		for proces in $procese; do
			rez="$rez$proces\n"
		done
	done
	final=`echo $rez | uniq -c | sort -n -r`
	echo $final
fi
