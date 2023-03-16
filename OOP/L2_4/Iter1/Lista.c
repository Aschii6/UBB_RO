//
// Created by Daniel on 12-Mar-23.
//

#include "Lista.h"
#include <string.h>
#include <assert.h>

Lista creeazaGoala(){
    Lista rez;
    rez.lg = 0;
    return rez;
}

void distrugeLista(Lista* l){
    // nu avem de dealocat nimic inca
    l->lg = 0;
}

ElemType elem(Lista* l, int poz){
    return l->elems[poz];
}

int dimensiune(Lista* l){
    return l->lg;
}

void adauga(Lista* l, ElemType el){
    l->elems[l->lg] = el;
    l->lg++;
}

void modifica(Lista* l, ElemType el){
    for (int i = 0; i < dimensiune(l); ++i) {
        if (elem(l, i).nr == el.nr){
            strcpy_s(l->elems[i].model, sizeof(el.model), el.model);
            strcpy_s(l->elems[i].categorie, sizeof(el.categorie), el.categorie);
        }
    }
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
    for (int i = 0; i < dimensiune(l); ++i) {
        ElemType el = elem(l, i);
        adauga(&rez, el);
    }
    return rez;
}

void testCreeazaLista(){
    Lista l = creeazaGoala();
    assert(dimensiune(&l) == 0);
}

void testIteratorLista(){
    Lista l = creeazaGoala();
    adauga(&l, creeazaMasina(15, "KIA", "Family"));
    adauga(&l, creeazaMasina(15, "Audi", "Sport"));
    assert(dimensiune(&l) == 2);

    Masina m = elem(&l, 0);
    assert(m.nr == 15);
    assert(strcmp(m.model, "KIA") == 0);

    m = elem(&l, 1);
    assert(strcmp(m.categorie, "Sport") == 0);

    distrugeLista(&l);
    assert(dimensiune(&l) == 0);
}

void testCopieLista() {
    Lista l = creeazaGoala();
    adauga(&l, creeazaMasina(5, "Ferrari", "Supercar"));
    adauga(&l, creeazaMasina(5, "Lamborghini", "Supercar"));
    Lista lCopie = copieLista(&l);
    assert(dimensiune(&lCopie) == 2);
    Masina m = elem(&l, 0);
    assert(m.nr == 5);
    assert(strcmp(m.categorie, "Supercar") == 0);
}
