#!/bin/bash
apt update && apt upgrade -y
apt install clang -y
clang --version