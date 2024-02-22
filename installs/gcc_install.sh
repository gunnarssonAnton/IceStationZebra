#!/bin/bash
apt update && apt upgrade -y
apt install gcc -y
gcc --version