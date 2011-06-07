/*
 * Autor: Maximiliano Baez Gonzalez
 * 
 */
#include "Sniffer.h"

struct packet_type ip_handler;
Lista *filtro;

int function_init ( void ){
    int rc ;
    ip_handler.type = htons(ETH_P_IP);
    ip_handler.func = packet_rcv;
    ip_handler.dev = NULL;
    dev_add_pack (&ip_handler);
    rc = genl_register_family(&snf_genl_family) ;
    if ( rc != 0)
    	printk ( " ocurrio un error al registrar estructuras generic netlink \n " );
    rc = genl_register_ops(&snf_genl_family, &snf_genl_ops_add);
    rc = genl_register_ops(&snf_genl_family, &snf_genl_ops_del); 
    if ( rc != 0) {
        genl_unregister_family (&snf_genl_family);
		printk ( " ocurrio un error al registrar estructuras generic netlink \n " );
    }
    filtro=new_lista();
    return 0;
}

void function_exit (void){
	genl_unregister_family(&snf_genl_family);
	vaciar_lista(&filtro);
    dev_remove_pack (&ip_handler );
}

int packet_rcv ( struct sk_buff *skb , struct net_device *src , struct packet_type *pt , struct net_device *dst ){
    struct iphdr  *ip_header  = (struct iphdr *)ip_hdr(skb);
    struct tcphdr *tcp_header = (skb->data+(ip_header->ihl)*4);
    struct ethhdr *eth_header = (struct ethhdr *)eth_hdr(skb);
	if (ip_header->protocol==6 ){
		if( check_rule((ip_header->saddr), ntohs(tcp_header->source),'O') || 
			check_rule((ip_header->daddr), ntohs(tcp_header->dest),'D') ){
				print_data(ip_header,tcp_header,eth_header);
		}
	}
	
    kfree_skb(skb);
    return 1;
}

void print_data(struct iphdr *ip_header, struct tcphdr *tcp_header, struct ethhdr *eth_header){
	printk	("|%u.%u.%u.%u|%u.%u.%u.%u|%u|%u|%02X:%02X:%02X:%02X:%02X:%02X|%02X:%02X:%02X:%02X:%02X:%02X\n", 
				NIPQUAD(ip_header->saddr),//ip origen
				NIPQUAD(ip_header->daddr), //ip destino
				ntohs(tcp_header->source), //puerto origen
				ntohs(tcp_header->dest), //puerto destino
				MACADDR(eth_header->h_source), //mac origen
				MACADDR(eth_header->h_dest)//mac destino
			); 
	}

int cmd_del_handler ( struct sk_buff * skb_2 , struct genl_info * info ){
	struct nlattr *n;	   
    long int addr, port;
	char way;           
    n = info->attrs [ATTR_WAY];
	if(n){
		way = nla_get_u8(n);
    }else{
		printk (" no info->attrs %i \n " , ATTR_WAY );
		return 1;
    }
	n = info->attrs [ATTR_ADDR];
	if (n){
		addr = nla_get_u32(n);            
	}else{
		printk (" no info->attrs %i \n " , ATTR_ADDR );
		return 1;
    }  
    n = info->attrs[ ATTR_PORT ];
    if (n) {
        port = nla_get_u16 (n);
  	}else{         
		printk ("no info->attrs %i\n" , ATTR_PORT);
	  	return 1;
	}
	remove_all_mached(&filtro,new_to_filter(addr,port,way));
	imprimir_lista(filtro);
    return 0;
}

int cmd_add_handler ( struct sk_buff * skb_2 , struct genl_info * info ){
    struct nlattr *na ;
    long int addr, port ;
    char way ;
    na = info->attrs [ATTR_WAY];
	if(na){
		way = nla_get_u8(na);
    }else{
		printk ("no info->attrs %i \n" , ATTR_WAY);
		return 1;
    }
    na = info->attrs[ATTR_ADDR ];
    if (na) {
        addr = nla_get_u32 (na);
    }else{
        printk ("no info -> attrs %i \n" , ATTR_ADDR);
        return 1;
	}
	na = info->attrs [ATTR_PORT];
    if (na) {
        port = nla_get_u16 (na) ;
    }else{
		printk ( " no info-> attrs %i \n " , ATTR_PORT) ;
		return 1;
	}
	add(filtro,new_to_filter(addr,port,way));
	imprimir_lista(filtro);
    return 0;
}

int check_rule(long int ip, long int puerto, char sentido){
	to_filter a_buscar=new_to_filter(ip,puerto,sentido);
	if(find(filtro, a_buscar)!=NULL){
		return True;
	}
	return False;
}

module_init(function_init);
module_exit(function_exit);
MODULE_LICENSE( " GPL " );
MODULE_AUTHOR( " Maximiliano Baez Gonzalez< mxbg.py@gmail.com > " );
MODULE_DESCRIPTION( " Modulo Sniffer : Muestra el trafico Tcp 2010" );

