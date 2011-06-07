#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <arpa/inet.h>

#include <errno.h>
#include <unistd.h>
#include <poll.h>
#include <string.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <signal.h>
#include <linux/genetlink.h>
#include <netlink/genl/genl.h>
#include <netlink/genl/ctrl.h>

#define VERSION_NR 1
#define CMD_MAX ( __CMD_MAX - 1)
#define ATTR_MAX ( __ATTR_MAX - 1)
 // atributos
enum {
	ATTR_UNSPEC ,
	ATTR_ADDR ,
	ATTR_PORT ,
	ATTR_WAY ,
    __ATTR_MAX ,
};
 // comandos
enum {
	CMD_UNSPEC ,
	CMD_ADD ,
	CMD_DEL ,
    __CMD_MAX ,
};

typedef enum _boolean{False, True} boolean;

void send_msg(long int ip, long int puerto, char sentido);
boolean read_data(long int *ip, long int *puerto, char *sentido, void (*fptr)());
int menu_principal();
void menu_ver_trafico();
void print_borrar_header();
void menu_crear_regla();
void menu_borrar_regla();
void add_rule();
void del_rule();
void new_comunication();
void system_pause();

long int translate_ip(char *str);
boolean check_sentido(char *dato);
int read_sentido(char *sentido, void (*fptr)());
long int read_puerto(void (*fptr)());
long int read_ip(void (*fptr)());

void extern_program(char *argv[]);
