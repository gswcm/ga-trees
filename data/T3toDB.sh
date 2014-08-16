#!/bin/bash

#--
# States are: 
# 0 - waitForCName 
# 1 - waitForAName
# 2 - waitForBName
# 3 - waitForDescription
# 4 - waitForCharacteristics
# 5 - waitForWood
# 6 - waitForUses
# 7 - waitForDistribution
#-- Local variables
state=0
count=1
dbName="ga-trees.db"
declare -A T
#-- Initialize database
rm -f "$dbName"	
./db_init.sh | sqlite3 -init db.config "$dbName"
#-- Main loop to read standard input on line-wise basis and fill in associative array 'T'
while read line; do
	key="$(echo "$line" | cut -d: -f1 | tr '[:upper:]' '[:lower:]' | tr -s " ")"
	value="$(echo "$line" | cut -d: -f2 | tr -s " ")"
	case "$key" in
		"description") 
			if [ $state -eq 3 ]; then
				T[description]="$(echo $value | sed 's/^ *\(..*\) *$/\1/')"
				state=4
			else
				echo "$count --> Waited for 'description' but received '$key'" 1>&2
			fi
			;;
		"key characteristics")
			if [ $state -eq 4 ]; then
				T[key]="$(echo $value | sed 's/^ *\(..*\) *$/\1/')"
				state=5
			else
				echo "$count --> Waited for 'key characteristics' but received '$key'" 1>&2
			fi
			;;
		"wood") 
			if [ $state -eq 5 ]; then
				T[wood]="$(echo $value | sed 's/^ *\(..*\) *$/\1/')"
				state=6
			else
				echo "$count  --> Waited for 'wood' but received '$key'" 1>&2
			fi
			;;
		"uses")
			if [ $state -eq 6 ]; then
				T[uses]="$(echo $value | sed 's/^ *\(..*\) *$/\1/')"
				state=7
			else
				echo "$count --> Waited for 'uses' but received '$key'" 1>&2
			fi
			;;
		"distribution") 
			if [ $state -eq 7 ]; then
				T[distribution]="$(echo $value | sed 's/^ *\(..*\) *$/\1/')"
				state=0
				sqlite3 -init db.config "$dbName" \
					"
					insert into tree_info 
						(description,characteristics,wood,uses,distribution) 
						values 
						('${T[description]}','${T[key]}','${T[wood]}','${T[uses]}','${T[distribution]}');
					insert into tree_name 
						(tree_common,tree_botanical,tree_alternative,tree_info_id) 
						values 
						('${T[cName]}','${T[bName]}','${T[aName]}',(select last_insert_rowid() from tree_info));
					"								
			else
				echo "$count --> Waited for 'distribution' but received '$key'" 1>&2
			fi
			;;
		*) if [ $state -eq 0 ]; then
				T[cName]="$key"
				state=1
			elif [ $state -eq 1 ]; then
				if echo "$key" | grep -q "^ *[(]..*[)] *$"; then					
					T[aName]="$(echo "$key" | sed 's/^ *[(]\(..*\)[)] *$/\1/')"
					state=2
				else
					T[aName]=""
					T[bName]="$key"
					state=3
				fi
			elif [ $state -eq 2 ]; then
				T[bName]="$key"
				state=3
			elif [ $state -eq 3 ]; then
				continue;
			else
				echo "$count --> Illegal state ($state) with '$key'" 1>&2
			fi			
			;;
	esac
	((count++))
done
#-- Update tree_name table to include association woth tree_group
while IFS="," read id low high; do 
	[ -z $id ] && continue
	sqlite3 -init db.config ga-trees.db "update tree_name set tree_group_id=$id where _id>=$low and _id<=$high;"				
done <<EOF
1,1,10
2,11,11
3,12,12
4,13,13
5,14,14
6,15,16
7,17,24
8,25,25
9,26,27
10,28,29
11,30,30
12,31,31
13,32,32
14,33,34
15,35,52
16,53,55
17,56,57
18,58,58
19,59,63
20,64,64
21,65,65
22,66,66
23,67,67
24,68,68
25,69,69
26,70,70
27,71,72
28,73,73
29,74,76
30,77,80
31,81,81
32,82,82
33,83,85
34,86,86
35,87,87
36,88,88
37,89,89
38,90,91
39,92,92
EOF

