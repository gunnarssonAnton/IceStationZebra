#!/bin/bash
if [ $# -eq 0 ]; then
    echo "There is no argument"
fi
echo "$@"
eval "$@"
