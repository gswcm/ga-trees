#!/bin/bash

dbname="ga-trees.db"

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

sqlite3 "$dbname" "drop table if exists quest_navigation; drop table if exists quest_data; drop table if exists tree_id_range; create table tree_id_range (range_id integer primary key autoincrement, low integer, high integer); CREATE TABLE quest_navigation (quest_id integer, y integer, n integer, y_range_id integer, n_range_id integer, foreign key (y_range_id) references tree_id_range (range_id), foreign key (n_range_id) references tree_id_range (range_id)); CREATE TABLE quest_data (quest_id integer primary key, quest_text text, pic_y text, pic_n text);"

i=0
while read line; do
	case "$((i%3))" in
		0) qid=$(echo "$line" | cut -d. -f1);
			qtext="$(echo "$line" | cut -d. -f2 | sed 's/^[ ]*//')";
			;;
		1) if echo "$line" | grep -q "ID [0-9]"; then
				yes_tree_id=$(echo "$line" | sed 's/^\(..*\)ID \(..*\)/\2/' | tr -cd '[0-9]-')
				yes_next_id=""
			else
				yes_next_id=$(echo "$line" | tr -cd '[0-9]')
				yes_tree_id=""
			fi
			;;
		2) if echo "$line" | grep -q "ID [0-9]"; then
				no_tree_id=$(echo "$line" | sed 's/^\(..*\)ID \(..*\)/\2/' | tr -cd '[0-9]-')
				no_next_id=""
			else
				no_next_id=$(echo "$line" | tr -cd '[0-9]')
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
			#echo "'$qid' '$qtext' '$yes_next_id' '$no_next_id' '$yes_tree_id' '$no_tree_id'"
			sqlite3 "$dbname" "insert into quest_data (quest_id, quest_text) values ('$qid', '$qtext');"
			;;
	esac
	((i++))
done < T1
