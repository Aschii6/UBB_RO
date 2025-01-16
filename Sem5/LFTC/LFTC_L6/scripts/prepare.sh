#!/bin/bash

bison -d spec.y
if [ $? -ne 0 ]; then
    echo "Error: Bison failed."
    exit 1
fi

flex spec.l
if [ $? -ne 0 ]; then
    echo "Error: Flex failed."
    exit 1
fi

g++ lex.yy.c spec.tab.c -o compiler.exe
if [ $? -ne 0 ]; then
    echo "Error: Compilation failed."
    exit 1
fi

exit 0
