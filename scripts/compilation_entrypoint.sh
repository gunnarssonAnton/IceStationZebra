#!/bin/bash

if [ -z "$COMPILER_NAME" ]; then
    echo "The COMPILER_NAME environment variable is not set. Please set it to proceed."
else
    echo "This is a compilation for the $COMPILER_NAME compiler."

    FILENAME="/installs/${COMPILER_NAME}_install.sh"

    chmod +x "$FILENAME"

    ./${FILENAME}
    eval $COMPILER_COMMAND
    echo "$FILENAME has been executed."
fi

exec bash