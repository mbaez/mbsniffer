#include "cliente_sniffer.h"

struct nl_handle *sock;
struct nl_msg *msg;
int family;

sem_t mutex;
int KILL=0;
/*OPREACION IP,PUERTO, DIRECCION
 *OPREACION: 1 ADD
 * 			 2 DEL
 */
int main (int argc, char *argv[]) {
	if(argc >1){
		extern_program(argv);
		return 0;
	}
	sem_init(&mutex,0,1);
	int exit=1;
	int choice;
	while(exit){
		choice=menu_principal();
		if(choice==1){
			menu_ver_trafico();
		}
		else if(choice==2){
			menu_crear_regla();
		}
		else if(choice==3){
			menu_borrar_regla();
		}
		else if (choice==4){
			system("clear");
			exit=0;
		}
	}
	return 0;
}
/*
 * 1) Ver Trafico
 * 2) Crear regla de filtro
 * 3) Eliminar Regla 
 * 4) Salir 
 */
void extern_program(char *argv[]){
	printf("XTERM %s\n", argv[1]);
	char *token = argv[1];
	new_comunication();
	if(strcmp(token,"ADD")==0)
		add_rule();
	else if(strcmp(token,"DEL")==0)
		del_rule();
	else
		exit(1);
	char *ipstr=argv[2];
	long int ip=translate_ip(ipstr);
	printf("IP:%s %ld\n",ipstr,ip);
	char *aux=argv[3];
	long int puerto=atol(aux);
	printf("PUERTO: %ld\n", puerto);
	char *sentido=argv[4];
	printf("WAY: %s\n", sentido);
	send_msg(ip,puerto,sentido[0]);
}

void new_comunication(){
	sock = nl_handle_alloc ();
	genl_connect ( sock );
	family = genl_ctrl_resolve ( sock , " SNF_GENL " );
	msg = nlmsg_alloc();
}
int menu_principal(){
	int choice=0;
	char tmp[4];
	while(!(choice>=1 && choice <=4)){
		system("clear");
		printf("\n.==============================================.");
		printf("\n|               Mbaez Sniffer                  |");
		printf("\n|==============================================|");
		printf("\n|1)Ver trafico                                 |");
		printf("\n|2)Crear regla                                 |");
		printf("\n|3)Borrar regla                                |");
		printf("\n|4)Salir                                       |");
		printf("\n\'==============================================\'");
		printf("\n>>");
		scanf("%s",tmp);
		choice=atoi(tmp);
	}
	return choice;
}

void *show_trafic(void *ptr){
	int kill=1; 
	while(kill){
		system("./Scripts/show_trafic.pl");	
		sem_wait(&mutex);
		if(KILL==1)
			kill=0;
		sem_post(&mutex);
		printf("=========================================================");
		printf("==========================================================");
		printf("\nPresione cualquier tecla para salir");
		printf("\n>>\n");	
		sleep(1);
	}
}

void menu_ver_trafico(){
	char choice[1];
	pthread_t p;
	pthread_create(&p, NULL, show_trafic,NULL);
	scanf("%s",choice);
	sem_wait(&mutex);
	KILL=1;
	sem_post(&mutex);
	printf("\nCerrando Espere...\n");
	pthread_join(p, NULL);
	KILL=0;
}

void print_crear_header(){
	system("clear");
	printf("\n.==============================================.");
	printf("\n|                 Crear Regla                  |");
}

void print_borrar_header(){
	system("clear");
	printf("\n.==============================================.");
	printf("\n|                Borrar Regla                  |");
}

void menu_crear_regla(){
	long int puerto;
	long int ip;
	char sentido[1];
	new_comunication();
	if(read_data(&ip,&puerto, sentido, print_crear_header)){
		add_rule();
		send_msg(ip,puerto,sentido[0]);
		system_pause();
	}
}

void menu_borrar_regla(){
	long int puerto;
	long int ip;
	char sentido[1];
	new_comunication();
	if(read_data(&ip,&puerto, sentido, print_borrar_header)){
		del_rule();
		send_msg(ip,puerto,sentido[0]);
		system_pause();
	}	
}

void add_rule(){
	genlmsg_put(msg, NL_AUTO_PID, NL_AUTO_SEQ, family, 0, NLM_F_ECHO, CMD_ADD, VERSION_NR);
}

void del_rule(){
	genlmsg_put(msg, NL_AUTO_PID, NL_AUTO_SEQ, family, 0, NLM_F_ECHO, CMD_DEL, VERSION_NR);
}

void send_msg(long int ip, long int puerto, char sentido){
	nla_put_u32( msg, ATTR_ADDR, ip);
	nla_put_u16( msg, ATTR_PORT, puerto);
	nla_put_u16( msg, ATTR_WAY, sentido);
	nl_send_auto_complete(sock, msg);
	printf("Peticion registrada :)");
}

boolean read_data(long int *ip, long int *puerto, char *sentido, void (*fptr)() ){
	if((*ip=read_ip(fptr))==-1)
		return False;
	if((*puerto=read_puerto(fptr))==-1)
		return False;
	if(read_sentido(sentido,fptr)==False)
		return False;
	return True;
}
void system_pause(){
	printf("\nPulse enter para continuar...");
	getchar();getchar();
	//system("read -p 0 -ers");
}

long int read_ip(void (*fptr)()){
	long int status=-1;
	char stdin[15];
	while(status==-1){
		fptr();
		printf("\n.==============================================.");
		printf("\n|IP Addres:                                    |");
		printf("\n|          Cualquiera: 0                       |");
		printf("\n|          Salir     : S/s                     |");
		printf("\n\'==============================================\'");
		printf("\n>> ");
		scanf("%s",stdin);
		if(stdin[0]=='s'|| stdin[0]=='S')
			return -1;
		status=translate_ip(stdin);
	}
	return status;
}
long int read_puerto(void (*fptr)()){
	char stdin[15];
	fptr();
	printf("\n.==============================================.");
	printf("\n|Puerto:                                       |");
	printf("\n|          Cualquiera: 0                       |");
	printf("\n|          Salir     : S/s                     |");
	printf("\n\'==============================================\'");
	printf("\n>> ");
	scanf("%s",stdin);
	if(stdin[0]=='s'|| stdin[0]=='S')
		return -1;
	return atol(stdin);
}
int read_sentido(char *stdin, void (*fptr)()){	
	int status=0;
	while(status==False){
		fptr();
		printf("\n.==============================================.");
		printf("\n|Sentido:                                      |");
		printf("\n|          Destino: D                          |");
		printf("\n|          Origen : O                          |");
		printf("\n|          All    : *                          |");
		printf("\n|          Salir  : S/s                        |");
		printf("\n\'==============================================\'");
		printf("\n>> ");
		scanf("%s",stdin);
		if(stdin[0]=='s'|| stdin[0]=='S')
			return False;
		status=check_sentido(stdin);
	}
	return True;
}
/*Retorna True o False
 */ 
boolean check_sentido(char *dato){
	if(strcmp(dato,"O")==0||strcmp(dato,"D")==0|| strcmp(dato,"*")==0)
		return True;
	return False;
		
}
/*Retorna -1 cuando hay error de candena mal compuesta
 * cualquier numero distinto a cero es correcto
 */ 
long int translate_ip(char *str){
	int len=0;
	char ipstr[15];
	strcpy(ipstr,str);
	char *token=strtok(str,".");
	while(token!=NULL){
		token = strtok(NULL, ".");
		len++;
	}
	if(len==4||strcmp(str,"0")==0){
		return inet_addr(ipstr);
	}
	else if(strcmp(str,"*")==0){
		return 0;
	}
	return -1;
}
