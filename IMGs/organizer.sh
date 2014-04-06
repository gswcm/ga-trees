#!/bin/bash

if [ $# -lt 1 ]  
then
	echo "Usage: $(basename $0) <file.csv>" 1>&2
	exit 1
fi

dstDir="organized"
srcDir="unsorted"
rm -rf "$dirName" 

[ -f "$1" ] && cat $1 | tail -n +2 | awk -F\" '{print $1 tolower($3)}' | awk -F, '{printf "%s,%s,%s\n",$1,$3,$7}' | sort -t, -k2 | sed 's|/|-|g' | { 
	IFS=","; 
	while read a b c; 
	do 
		[ -z "$a" -o -z "$b" -o -z "$c" ] && continue;
		[ -d "$dstDir/$b" ] || mkdir -p "$dstDir/$b"; 
		[ -f "$srcDir/$a.jpg" ] && { 
			cd "$dstDir/$b"; 
			if [ -f "${c}-00.jpg" ]
			then
				i=1
			   while true	
				do
					fname="$(printf "%s-%02d" "$c" $i).jpg"
					if [ ! -f "$fname" ]
					then
						break;
					fi
					i=$(expr $i + 1)
				done
				ln "../../$srcDir/$a.jpg" "./$fname";
			else
				ln "../../$srcDir/$a.jpg" "./$c-00.jpg"; 
			fi
			cd ../..; 
		}; 
	done; 
}

