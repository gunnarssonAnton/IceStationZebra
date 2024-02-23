#!/bin/bash
echo "Listening to stdin..."

while true; do
  read line
  echo "You typed: $line"
  sleep 1
done

#while true; do
#  read line
#  if [ -n "$line" ]; then
#      echo $line
#  fi
#
#  if [ -z "$line" ]; then
#    sleep 1
#  fi
#  echo "hej"
#done

#while IFS= read -r line; do
#  if [ "$line" = "exit" ]; then
#    break
#  fi
#  echo "Received: $line"
#done
#echo "Listening to stdin..."
