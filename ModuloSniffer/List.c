#include<linux/slab.h>

typedef enum _boolean{False, True} boolean;
/*
 * Estructura to_filter contiene los atributos utilizados para filtrar
 */
typedef struct __to_filter{
	long int addr;
    long int port;
    char way;
}to_filter;

to_filter null_filter= {-1,-1,'-1'};

to_filter new_to_filter(long int addr, long int port, char way){
	to_filter f;
	f.addr=addr;
	f.port=port;
	f.way=way;
	return f;
}

boolean is_equal(to_filter d1, to_filter d2){
	return ((d1.addr==d2.addr || d1.addr==0 || d2.addr==0) && 
			(d1.port==d2.port || d1.port==0 || d2.port==0) && 
			(d1.way==d2.way || d1.way=='*' || d2.way=='*'));
}

boolean is_null(to_filter d){
	return is_equal(d,null_filter);
}

/*
 * Estructuras y funciones sobre el Nodo
 */
typedef struct _Nodo{
    to_filter dato;
    struct _Nodo *sgt;
}Nodo;

Nodo *new_nodo(to_filter dato){
    Nodo *e=(Nodo *)kmalloc(sizeof(Nodo),GFP_KERNEL);
    e->dato=dato;
    e->sgt=NULL;
    return e;
}

Nodo *find_nodo(Nodo *cima, to_filter dato){
	if(cima!=NULL){
		if(is_equal(cima->dato,dato)){
			return cima;
		}
		return find_nodo(cima->sgt,dato);
	}
	return NULL;
}

void imprimir_nodos(Nodo *cima){
	if(cima!=NULL){
		printk("\n%u.%u.%u.%u->", NIPQUAD(cima->dato.addr));
		imprimir_nodos(cima->sgt);
	}
}

/*
 * Estructuras y funciones sobre la lista
 */
typedef struct _Lista{
    Nodo *cima;
}Lista;

Lista *new_lista( ){
    Lista *l=(Lista *)kmalloc(sizeof(Lista),GFP_KERNEL);;
    l->cima=NULL;
    return l;
}

boolean es_vacia(Lista *l){
    return (l->cima==NULL);
}

void add(Lista *l, to_filter dato){
	Nodo *tmp=new_nodo(dato);
	if(es_vacia(l)){
        l->cima=tmp;
	}
	else{
		tmp->sgt=l->cima;
		l->cima=tmp;
    }
}

Nodo *find(Lista *l,to_filter dato){
	if(es_vacia(l)){
		return NULL;
	}
	return find_nodo(l->cima, dato);
}

Nodo **list_search(Nodo **cima, to_filter dato) {
    if (*cima != NULL) {
        if (is_equal((*cima)->dato, dato)) {
            return cima;
        }
        return list_search(&(*cima)->sgt, dato);
    }
    return NULL;
}

boolean remove_dato(Lista **l, to_filter dato) { 
	Nodo **cima = list_search(&((*l)->cima),dato);
    if (*cima != NULL) {
		if((*cima)->sgt==NULL){
			*cima=NULL;
		}
		else{
			Nodo **tmp=&(*cima)->sgt;
			*cima = *tmp;
		}
		return True;
    }
	return False;
}

boolean remove(Lista **l, to_filter dato){
	if(es_vacia(*l)){
		return False;
	}
	return remove_dato(&(*l), dato);
}

void remove_all_mached(Lista **l,to_filter dato){
	boolean end=True;
	int i=1;
	while (end){
		end=remove(&(*l),dato);
		printk("\nRegla/s %d Eliminada/s", i);
		i++;
	}
}

void vaciar_lista(Lista **l){
	remove_all_mached(&(*l), new_to_filter(0,0,'*'));
}

void imprimir_lista(Lista *l){
	printk("\nElementos: ");
	imprimir_nodos(l->cima);
	printk("\n");
}



 


