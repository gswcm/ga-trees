#!/bin/bash

declare -A T

file="T5a"
[ $# -gt 0 ] && [ -f "$1" ] && file="$1"

sqlite3 ga-trees.db "delete from tree_desc; delete from tree_main;"

while read line; do
	key="$(echo "$line" | cut -d: -f1 | tr '[:upper:]' '[:lower:]')"
	val="$(echo "$line" | cut -d: -f2 | sed 's/^[ ]*\(..*\)$/\1/')"
	case "$key" in
		"description") 			T[desc]="$val";;
		"key characteristics") 	T[keys]="$val";;
		"uses") 						T[uses]="$val";;
		"wood") 						T[wood]="$val";;
		"distribution") 			T[dist]="$val";;
		*) 							T[name]="$(echo "$val" | tr '[:upper:]' '[:lower:]')";  [ ${#T[@]} -eq 6 ] && { 
			#printf "Name: %s\n\n" "${T[name]}";
			#printf "Desc: %s\n\n" "${T[desc]}";
			#printf "Keys: %s\n\n" "${T[keys]}";
			#printf "Uses: %s\n\n" "${T[uses]}";
			#printf "Wood: %s\n\n" "${T[wood]}";
			#printf "Dist: %s\n\n" "${T[dist]}";
			#sqlite3 -init ga-trees.config ga-trees.db "insert into tree_desc (full) ('${T[desc]}'); insert into tree_main(cName,bName,key,wood,uses,dist,desc_id) values (${T[name]},${T[name]},${T[keys]},${T[wood]},${T[uses]},${T[dist]},(select last_insert_rowid() from tree_desc));" 
			sqlite3 -init ga-trees.config ga-trees.db "insert into tree_desc (full) values ('${T[desc]}'); insert into tree_main(cName,bName,key,wood,uses,dist,desc_id) values ('${T[name]}','${T[name]}','${T[keys]}','${T[wood]}','${T[uses]}','${T[dist]}',(select last_insert_rowid() from tree_desc));" 
		};;
	esac
done < <(tac "$file")
