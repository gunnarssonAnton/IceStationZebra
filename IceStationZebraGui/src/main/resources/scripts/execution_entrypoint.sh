#!/bin/bash

# Check if the first argument is provided and is a number
echo "$@"
make -C /RAPL/ Makefile main
if ! [[ "$1" =~ ^[0-9]+$ ]]; then
    echo "Usage: $0 <number_of_iterations>"
    exit 1
fi




    # Execute scripts
#    python3 /scripts/scope.py "${@:1}"
    python3 /scripts/scope2.py "$@"
