#include <linux/netdevice.h>
#include <linux/if_ether.h>
#include <net/genetlink.h>
#include <net/netlink.h> 
#include <linux/skbuff.h>
#include <linux/socket.h>
#include <linux/module.h>
#include <linux/init.h>
#include <linux/tcp.h>
#include <linux/ip.h>
#include <linux/types.h>

#include "List.c"

#define VERSION_NR 1
#define ATTR_MAX ( __ATTR_MAX - 1)
#define CMD_MAX ( __CMD_MAX - 1)
#define MACADDR(addr) \
((unsigned char *)&addr)[0],\
((unsigned char *)&addr)[1],\
((unsigned char *)&addr)[2],\
((unsigned char *)&addr)[3],\
((unsigned char *)&addr)[4],\
((unsigned char *)&addr)[5]

int packet_rcv (struct sk_buff *skb , struct net_device *src , struct packet_type *pt ,struct net_device *dst );

void print_data(struct iphdr *ip_header, struct tcphdr *tcp_header, struct ethhdr *eth_header);

int cmd_add_handler ( struct sk_buff *skb_2 , struct genl_info *info );

int cmd_del_handler ( struct sk_buff *skb_2 , struct genl_info *info );

int check_rule(long int ip, long int puerto, char sentido);

enum {
	ATTR_UNSPEC ,
    ATTR_ADDR ,//Direccion
    ATTR_PORT ,//Puerto
    ATTR_WAY ,//Sentido
    __ATTR_MAX ,
};

enum {
    CMD_UNSPEC ,
    CMD_ADD ,
    CMD_DEL ,
    __CMD_MAX ,
};

static struct nla_policy snf_genl_policy [ ATTR_MAX + 1] = {
	[ ATTR_ADDR ] = { .type = NLA_U32 } ,
    [ ATTR_PORT ] = { .type = NLA_U16 } ,
    [ ATTR_WAY ] = { .type = NLA_U8 } ,
};

static struct genl_family snf_genl_family = {
    .id = GENL_ID_GENERATE ,      //genetlink tiene que generar el id
    .hdrsize = 0 ,
    .name = " SNF_GENL " ,        //nombre de la familia
    .version = VERSION_NR ,       //numero de version
    .maxattr = ATTR_MAX ,         // Cantidad maxima de atributos
};

struct genl_ops snf_genl_ops_add= {
   .cmd = CMD_ADD,
   .flags = 0,
   .policy = snf_genl_policy,
   .doit = cmd_add_handler,
   .dumpit = NULL,
};

struct genl_ops snf_genl_ops_del = {
	.cmd = CMD_DEL,
	.flags = 0,
	.policy = snf_genl_policy,
	.doit = cmd_del_handler,
	.dumpit = NULL,
};
