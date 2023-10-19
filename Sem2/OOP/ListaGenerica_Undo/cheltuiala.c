#include "cheltuiala.h"
#include <string.h>
#include <malloc.h>

Cheltuiala* constructor_cheltuiala(int id, int zi, float suma, char* tip){
    Cheltuiala* c = malloc(sizeof(Cheltuiala));
    c->id = id;
    c->zi = zi;
    c->suma = suma;
    c->tip = (char*)malloc((strlen(tip)+1) * sizeof(char));
    strcpy(c->tip, tip);
    return c;
}

void destructor_cheltuiala(Cheltuiala* c){
    free(c->tip);
    free(c);
}

Cheltuiala* copy_cheltuiala(Cheltuiala* c){
    return constructor_cheltuiala(c->id, c->zi, c->suma, c->tip);
}
