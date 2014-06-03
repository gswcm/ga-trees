#!/bin/bash

# Call to SK.sh to remove all navigation related tables and populate DB with SK data
WK_dir=$(pwd)
cd ../Summer_Key
./SK.sh
cd $WK_dir

dbname="../ga-trees.db"
keyname="Winter Key.txt"
qoffset=1000

add_range() {
	if [ -z "$1" ] ; then
		return 1
	fi
	low=$(echo "$1" | cut -d"-" -f1)
	high=$(echo "$1" | cut -d"-" -f2)
	sqlite3 "$dbname" "insert into tree_id_range (low, high) values ('$low', '$high');"
	sqlite3 "$dbname" "select max(rowid) from tree_id_range;"
	return 0
}

sed '/^\s*$/d' "$keyname" | tr -cd '[a-z][A-Z][0-9] ?.\n-' | tr -s ' ' | sed 's/^ //' | sed '/[0-9][0-9]*/!d' > WK

i=0
while read line; do
	case "$((i%3))" in
		0) qid=$(expr $qoffset + $(echo "$line" | cut -d. -f1));
			qtext="$(echo "$line" | cut -d. -f2 | sed 's/^[ ]*//')";
			;;
		1) if echo "$line" | grep -q "ID [0-9]"; then
				yes_tree_id=$(echo "$line" | sed 's/^\(..*\)ID \(..*\)/\2/' | tr -cd '[0-9]-')
				yes_next_id=""
			else
				yes_next_id=$(expr $qoffset + $(echo "$line" | grep -Eo "[-]?[0-9]+$"))
				yes_tree_id=""
			fi
			;;
		2) if echo "$line" | grep -q "ID [0-9]"; then
				no_tree_id=$(echo "$line" | sed 's/^\(..*\)ID \(..*\)/\2/' | tr -cd '[0-9]-')
				no_next_id=""
			else
				no_next_id=$(expr $qoffset + $(echo "$line" | grep -Eo "[-]?[0-9]+$"))
				no_tree_id=""
			fi
			
			yes_range_id=$(add_range "$yes_tree_id")
			result=$?
			no_range_id=$(add_range "$no_tree_id")
			result=$result$?
			case "$result" in
				"00") sqlite3 "$dbname" "insert into quest_navigation (quest_id, y, n, y_range_id, n_range_id) values ('$qid', null, null, '$yes_range_id', '$no_range_id');";;
				"01") sqlite3 "$dbname" "insert into quest_navigation (quest_id, y, n, y_range_id, n_range_id) values ('$qid', null, '$no_next_id', '$yes_range_id', null);";;
				"10") sqlite3 "$dbname" "insert into quest_navigation (quest_id, y, n, y_range_id, n_range_id) values ('$qid', '$yes_next_id', null, null, '$no_range_id');";;
				"11") sqlite3 "$dbname" "insert into quest_navigation (quest_id, y, n, y_range_id, n_range_id) values ('$qid', '$yes_next_id', '$no_next_id', null, null);";;
			esac
			#echo "'$qid' '$qtext' '$yes_next_id' '$no_next_id' '$yes_tree_id' '$no_tree_id' '$yes_range_id' '$no_range_id'"
			sqlite3 "$dbname" "insert into quest_data (quest_id, quest_text) values ('$qid', '$qtext');"
			;;
	esac
	((i++))
done < WK
