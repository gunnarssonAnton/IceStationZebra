#!/bin/bash
echo "Post execution script round: $ROUND"
/files/togglePin 16 0

if [ "$ROUND" -lt "$1" ]; then
  /files/togglePin 21 0
fi
