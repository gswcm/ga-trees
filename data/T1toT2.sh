#!/bin/bash

sed 's/^[^<]*<i>[^a-zA-Z ]*\([a-zA-Z -][a-zA-Z -]*\)..*<[/]i>..*$/\1/' | 
sed 's/<br[/]>/\n/g' | 
sed '/^[ ]*[0-9]*[ ]*$/d' | 
sed 's/&#160;/ /g' | 
sed '/<a name..*[/]>/d' | 
sed 's/<[/]*[a-z][a-z]*[/]*>//g'

