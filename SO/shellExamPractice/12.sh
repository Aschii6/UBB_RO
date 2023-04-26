#!/bin/sh

DIR=$1

files=`find $DIR -type f | grep -E "*.c$"`

total=0
for file in $files; do
	if test -f $file; then
		nr_lines=$(grep -E -c -v "^[ \t]*$" "$file")
		total=$((total+nr_lines))
	fi
done

echo $total
