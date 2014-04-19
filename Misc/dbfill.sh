#!/bin/bash

file=T4

[ $# -gt 0 ] && file="$1"

[ ! -f "$file" ] && {
	echo "File '$file' cannot be read";
	exit 1; 
} 1>&2 

temp=""
while read line
do
	if echo "$line" | grep -q "^[A-Z -]\{2,\}$"; then
		# name of a tree
		[ ${#temp} -gt 0 ] && echo "$temp" | tr -s ' ' | sed 's/- /-/g'
		echo "$line"
		temp=""
	elif echo "$line" | grep -q "^[A-Z ]\{1,\}:"; then
		# 'description', 'uses', 'wood', 'key characteristics' or 'distribution'
		[ ${#temp} -gt 0 ] && echo "$temp" | tr -s ' ' | sed 's/- /-/g'
		temp="$line"
	else
		temp="$temp $line"
	fi
done < "$file"
[ ${#temp} -gt 0 ] && echo "$temp" | tr -s ' ' | sed 's/- /-/g'

