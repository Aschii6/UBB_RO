#!/bin/sh

string=""
lines=`cat $1`

for line in $lines; do
	string=$string$line"@ubbcluj.ro,"
done

rez=`echo $string | sed -E "s/\,$//"`
echo $rez
