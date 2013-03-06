#!/bin/bash

# ucflif.sh
# Upper Case First Letter In Filename
# By Simon Pratt

for d in `ls`; do
	if [ -d $d ]; then
		cd $d
		for f in `ls`; do
			if [ -f $f ]; then
				fu=`echo $f | python -c "print raw_input().capitalize()"`
				mv $f $fu
			fi
		done
		cd ..
	fi
done
