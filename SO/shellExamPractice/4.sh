#!/bin/sh

links=`find -L dir`

for link in $links; do
	if [ ! -e $link ]; then
		echo $link nu mai exista
	fi
done
