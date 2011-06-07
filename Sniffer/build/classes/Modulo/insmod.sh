#!/bin/sh
cd ./src/Modulo
pwd
make clean;
make all;
sudo insmod Sniffer.ko;
