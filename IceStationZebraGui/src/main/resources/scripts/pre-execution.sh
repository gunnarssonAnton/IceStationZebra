#!/bin/bash
echo "Pre execution script round: $ROUND"

if [ "$ROUND" -lt "0" ]; then
  /files/togglePin 21 1
fi

/files/togglePin 16 1
