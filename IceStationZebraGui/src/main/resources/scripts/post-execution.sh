#!/bin/bash
echo "Post execution script round: $ROUND"
/files/togglePin 16 0

if [ "$ROUND" -lt "$2" ]; then
  /files/togglePin 20 0
fi
