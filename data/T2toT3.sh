#!/bin/bash
buf=""
flag=0
sed '/^[ ]*FAMILY..*$/d' | sed '/^[ ]*$/d' > $$
while read line; do  
	if echo "$line" | grep -q "^[A-Z]\{2,\}"; then
		flag=0
		if [ ! -z "$buf" ]; then
			echo "$buf"
			buf=""
		fi
		if echo "$line" | grep -q -e "^DESC" -e "^KEY" -e "^WOOD" -e "^USES" -e "^DISTR"; then
			flag=1
		fi
	fi
	if [ $flag -eq 0 ]; then
		echo "$line" 
	else
		[ -z "$buf" ] && buf="$line" || buf="$buf $line"
	fi
done < $$
echo "$buf"
rm -f $$
