#!/bin/sh

find dir -type f | grep -E -c "*.c$"
