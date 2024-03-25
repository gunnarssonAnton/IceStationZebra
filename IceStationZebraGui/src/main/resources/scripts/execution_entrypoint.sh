#!/bin/bash

# Check if the first argument is provided and is a number
if ! [[ "$2" =~ ^[0-9]+$ ]]; then
    echo "Usage: $0 <number_of_iterations>"
    exit 1
fi


while [ "$ROUND" -lt "$2" ]
do
    echo "ROUND is currently at $ROUND. Target is $2."

    # Execute scripts
    /scripts/pre-execution.sh
    /scripts/execution.sh "$@"
    /scripts/post-execution.sh

    # Increment ROUND
    ROUND=$((ROUND + 1))

    # It's crucial to export ROUND again to ensure the updated value is accessible
    # by subprocesses started in the next iteration of the loop
    export ROUND
done

echo "Completed. ROUND is now $ROUND."