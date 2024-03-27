#!/bin/bash

# Check if the first argument is provided and is a number
echo "$@"
if ! [[ "$1" =~ ^[0-9]+$ ]]; then
    echo "Usage: $0 <number_of_iterations>"
    exit 1
fi

    # Compile togglePin
    gcc /files/togglePin.c -lgpiod -o /files/togglePin
    chmod +x /files/togglePin

while [ "$ROUND" -lt "$1" ]
do
    echo "ROUND is currently at $ROUND. Target is $1."



    # Execute scripts
    /scripts/pre-execution.sh
    /scripts/execution.sh "${@:2}"
    /scripts/post-execution.sh "$1"

    # Increment ROUND
    ROUND=$((ROUND + 1))

    # It's crucial to export ROUND again to ensure the updated value is accessible
    # by subprocesses started in the next iteration of the loop
    export ROUND
done
ROUND="0"
export ROUND
echo "Completed. ROUND is now $ROUND."