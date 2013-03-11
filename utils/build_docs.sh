#!/bin/bash
if [ ! -d docs ]; then
	echo "Run this from project root."
	exit 1
fi
doxygen src/doxygen/doxygen.config && cd docs/latex && make && cp refman.pdf ..