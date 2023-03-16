//
// Created by Daniel on 09-Mar-23.
//

#include "Masina.h"
#include <string.h>
#include <assert.h>

Masina creeazaMasina(int nr, char* model, char* categorie){
    Masina rez;
    rez.nr = nr;
    strcpy_s(rez.model,sizeof(rez.model),model);
    strcpy_s(rez.categorie,sizeof(rez.categorie),categorie);
    rez.inchiriata = 0 ;
    return rez;
}

void distrugeMasina(Masina* m){
    // nu avem memorie pe heap, deci nu avem ce elibera
    m->nr = -1;
    m->model[0] = '\0';
    m->categorie[0] = '\0';
}

int valideaza(Masina m){
    if (m.nr <= 0)
        return 1;
    if (strlen(m.model) == 0)
        return 2;
    if (strlen(m.categorie) == 0)
        return 3;
    return 0;
}

void testeCreeazaDistruge(){
    Masina m = creeazaMasina(520, "KIA", "SUV");
    assert(m.nr == 520);
    assert(strcmp(m.model, "KIA") == 0);
    assert(strcmp(m.categorie, "SUV") == 0);

    distrugeMasina(&m);
    assert(m.nr == -1);
    assert(strlen(m.model) == 0);
    assert(strlen(m.categorie) == 0);
}
