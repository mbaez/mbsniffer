all: cliente_sniffer
#Construccion del cliente_sniffer
cliente_sniffer: obj/cliente_sniffer.o
	mkdir -p obj
	gcc obj/cliente_sniffer.o -ocliente_sniffer.bin -lrt -lnl -lpthread
obj/cliente_sniffer.o: src/cliente_sniffer.c
	mkdir -p obj
	gcc -c src/cliente_sniffer.c -oobj/cliente_sniffer.o -Iinclude

clean: clean-backcups clean-obj clean-bin

clean-backcups: 
	rm -f src/*.c~ include/*.h~ Makefile~ *~

clean-obj:
	rm -fr obj obj

clean-bin:
	rm -f *.bin
