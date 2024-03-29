//
// Created by Daniel on 12-Mar-23.
//

#include "Lista.h"
#include <stdlib.h>
#include <string.h>
#include <assert.h>

Lista creeazaGoala(){
    Lista rez;
    rez.lg = 0;
    rez.cap = 1;
    rez.elems = malloc(sizeof (ElemType)* rez.cap);
    return rez;
}

void distrugeLista(Lista* l){
    // dealocare masini din lista apoi lista
    for (int i = 0; i < l->lg; i++) {
        distrugeMasina(&l->elems[i]);
    }
    free(l->elems);
    l->elems = NULL;
    l->lg = 0;
}

ElemType elem(Lista* l, int poz){
    return l->elems[poz];
}

int dimensiune(Lista* l){
    return l->lg;
}

void asiguraCapacitate(Lista* l){
    if (l->lg == l->cap){
        l->cap *= 2;
        ElemType* altElems = malloc(sizeof (ElemType)* l->cap);
        for (int i = 0; i < l->lg; i++) {
            altElems[i] = l->elems[i];
        }
        free(l->elems);
        l->elems = altElems;
    }
}

void adauga(Lista* l, ElemType el){
    asiguraCapacitate(l);
    l->elems[l->lg] = el;
    l->lg++;
}

Masina modifica(Lista* l, int poz, ElemType el){
    Masina rez = elem(l, poz);
    l->elems[poz] = el;
    return rez;
}

int inchiriaza(Lista* l, int nr){
    for (int i = 0; i < dimensiune(l); ++i)
        if(elem(l, i).nr == nr){
            if(elem(l, i).inchiriata == 0){
                l->elems[i].inchiriata = 1;
                return 0;
            } else
                return 2;
        }
    return 1;
}

int returneaza(Lista* l, int nr){
    for (int i = 0; i < dimensiune(l); ++i)
        if(elem(l, i).nr == nr){
            if(elem(l, i).inchiriata == 1){
                l->elems[i].inchiriata = 0;
                return 0;
            } else
                return 2;
        }
    return 1;
}

Lista copieLista(Lista* l){
    Lista rez = creeazaGoala();
    for (int i = 0; i < dimensiune(l); i++) {
        ElemType el = elem(l, i);
        adauga(&rez, copieMasina(&el));
    }
    return rez;
}

void testCreeazaLista(){
    Lista l = creeazaGoala();
    assert(dimensiune(&l) == 0);
    distrugeLista(&l);
}

void testIteratorLista(){
    Lista l = creeazaGoala();
    adauga(&l, creeazaMasina(15, "KIA", "Family"));
    adauga(&l, creeazaMasina(15, "Audi", "Sport"));
    assert(dimensiune(&l) == 2);

    Masina m1 = elem(&l, 0);
    assert(m1.nr == 15);
    assert(strcmp(m1.model, "KIA") == 0);

    Masina m2 = elem(&l, 1);
    assert(strcmp(m2.categorie, "Sport") == 0);

    distrugeLista(&l);
    assert(dimensiune(&l) == 0);

    distrugeLista(&l);
}

void testCopieLista() {
    Lista l = creeazaGoala();
    adauga(&l, creeazaMasina(5, "Ferrari", "Supercar"));
    adauga(&l, creeazaMasina(5, "Lamborghini", "Supercar"));

    Lista lCopie = copieLista(&l);
    assert(dimensiune(&lCopie) == 2);
    Masina m = elem(&lCopie, 0);
    assert(m.nr == 5);
    assert(strcmp(m.categorie, "Supercar") == 0);

    distrugeLista(&l);
    distrugeLista(&lCopie);
}
