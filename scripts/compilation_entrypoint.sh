#!/bin/bash

if [ -z "$EVENT_NAME" ]; then
    echo "The EVENT_NAME environment variable is not set. Please set it to proceed."
else
    echo "This is a compilation for the $EVENT_NAME compiler."

#    FILENAME="/installs/${COMPILER_NAME}_install.sh"

#    chmod +x "$FILENAME"

#    ./${FILENAME}
    echo "Name: $EVENT_NAME"
    echo "Install: $EVENT_INSTALL"
    echo "Compile command: $EVENT_COMPILE_COMMAND"

    eval $EVENT_INSTALL
    eval $EVENT_COMPILE_COMMAND
    echo "$EVENT_NAME done."
fi

exec bash