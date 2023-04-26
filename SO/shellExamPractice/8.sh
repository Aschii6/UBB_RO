#!/bin/sh

cat df.fake | sed -E "s/[M%]//g" | awk '{if(NR!=1){if(($2<1024)||($5 > 80)){print $6}}}'
