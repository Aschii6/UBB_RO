#!/bin/sh

while [ true ]; do
	for elem in $@; do
		procese=`cat ps.fake | grep $elem`
		for proces in $procese;do
			kill -9 $proces
		done
	done
	sleep 5
done
