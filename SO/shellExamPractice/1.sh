#!/bin/sh
users=`cat who.fake | awk '{print $1}' | sort | uniq`

for user in $users; do
	cat ps.fake | awk -v tmp=$user 'BEGIN{nr=0}{if($1==tmp)nr=nr+1;}END{print tmp " " nr}'
done
