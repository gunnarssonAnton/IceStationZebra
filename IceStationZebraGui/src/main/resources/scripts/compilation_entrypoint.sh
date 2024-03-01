#!/bin/bash

if [ -z "$EVENT_NAME" ]; then
    echo "The EVENT_NAME environment variable is not set. Please set it to proceed."
else
    echo "This is a compilation for the $EVENT_NAME compiler."

    echo "Name: $EVENT_NAME"
    echo "Compile command: $EVENT_COMPILE_COMMAND"
    echo "[COMPILING $EVENT_NAME]"
    eval $EVENT_COMPILE_COMMAND
    echo "$EVENT_NAME done."
    echo "Compile command: $EVENT_COMPILE_COMMAND"
fi

exec bash