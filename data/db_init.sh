#!/bin/bash

# Filename of the SQLite3 database from James development branch
dbName="trees.db"

cat <<EOF
BEGIN TRANSACTION;
CREATE TABLE tree_name (
	_id INTEGER PRIMARY KEY AUTOINCREMENT, 
	tree_common TEXT NOT NULL, 
	tree_botanical TEXT NOT NULL, 
	tree_alternative TEXT, 
	tree_info_id INTEGER,
	tree_group_id INTEGER,
	FOREIGN KEY (tree_info_id) REFERENCES tree_info (_id), 
	FOREIGN KEY (tree_group_id) REFERENCES tree_group (_id)
);
CREATE TABLE tree_group (
	_id INTEGER PRIMARY KEY AUTOINCREMENT, 
	group_common TEXT NOT NULL, 
	group_botanical TEXT NOT NULL
);
CREATE TABLE tree_info (
	_id INTEGER PRIMARY KEY AUTOINCREMENT, 
	description TEXT NOT NULL, 
	characteristics TEXT,
	wood TEXT,
	uses TEXT,
	distribution TEXT
);
CREATE TABLE quest_data (
	_id INTEGER PRIMARY KEY, 
	quest_text TEXT, 
	yes_pic TEXT, 
	no_pic TEXT, 
	yes_label TEXT, 
	no_label TEXT
);
CREATE TABLE tree_range (
	_id INTEGER PRIMARY KEY AUTOINCREMENT, 
	low_tree_name_id INTEGER, 
	high_tree_name_id INTEGER
);
CREATE TABLE quest_navigation (
	_id INTEGER, 
	yes_quest_id INTEGER, 
	no_quest_id INTEGER, 
	yes_range_id INTEGER, 
	no_range_id INTEGER, 
	FOREIGN KEY (yes_range_id) REFERENCES tree_range (_id), 
	FOREIGN KEY (no_range_id) REFERENCES tree_range (_id)
);
COMMIT;
EOF
echo "BEGIN TRANSACTION;"
sqlite3 "$dbName" "select * from quest_data" | awk -F"|" '{printf "INSERT INTO \"quest_data\" VALUES(%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\");\n",$3,$4,$5,$6,$2,$1}' | sed 's/""/NULL/g'
sqlite3 "$dbName" ".dump tree_id_range" | grep "^INSERT" | sed 's/tree_id_range/tree_range/'
sqlite3 "$dbName" ".dump quest_navigation" | grep "^INSERT"
sqlite3 "$dbName" "select * from tree_group" | awk -F"|" '{printf "INSERT INTO \"tree_group\" VALUES(%d,\"%s\",\"%s\");\n",$1,$2,$3}'
echo "COMMIT;"
