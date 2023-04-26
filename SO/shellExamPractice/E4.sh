#!/bin/sh

fisier=$1

dim=`du -b $fisier | awk '{print $1}'`
count=`cat $fisier | head -1 | wc -w`
echo $fisier : $dim $count
