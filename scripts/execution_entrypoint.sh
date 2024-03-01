#!/bin/bash
/scripts/pre-execution.sh
/scripts/execution.sh "$@"
/scripts/post-execution.sh
