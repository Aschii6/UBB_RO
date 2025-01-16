#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <input_file>"
    exit 1
fi

input_file=$1

./compiler.exe < "$input_file"
if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

nasm -f elf32 output.asm -o output.o
if [ $? -ne 0 ]; then
    echo "Error: Assembly failed."
    exit 1
fi

gcc -m32 -c main.c -o main.o
if [ $? -ne 0 ]; then
    echo "Error: C compilation failed."
    exit 1
fi

gcc -m32 -no-pie main.o output.o -o program.exe
if [ $? -ne 0 ]; then
    echo "Error: Linking failed."
    exit 1
fi

./program.exe
EXIT_CODE=$?

echo "Program exited with code $EXIT_CODE."
exit $EXIT_CODE

