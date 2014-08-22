#!/bin/bash

dir="processed"
mkdir -p "$dir/preview"

for f in *.png; do
	convert "$f" \( +clone -fx 'p{0,0}' \) -compose Difference  -composite -modulate 100,0  -alpha off  $$_diff.png
	convert $$_diff.png -bordercolor black -border 5 -threshold 0  -blur 0x3  $$_halo.png
	convert "$f" -bordercolor white -border 5 $$_halo.png -alpha Off -compose CopyOpacity -composite "$dir/$f" 
	convert "$dir/$f" -background black -flatten "$dir/preview/$f"
done
rm -f $$*

