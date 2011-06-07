#!/bin/bash
function make_proyect(){
	make clean;
	make all;
}
clear;
make_proyect
echo "Pulse enter para continuar..."
read -n 0 -ers
sudo insmod Sniffer.ko;
cd Cliente;
make_proyect
./cliente_sniffer.bin
sudo rmmod Sniffer
