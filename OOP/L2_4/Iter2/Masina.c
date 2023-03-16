//
// Created by Daniel on 09-Mar-23.
//

#include "Masina.h"
#include <stdlib.h>
#include <string.h>
#include <assert.h>


Masina creeazaMasina(int nr, char* model, char* categorie){
    Masina rez;
    rez.nr = nr;
    rez.model = malloc(sizeof(char)* (strlen(model) + 1));
    strcpy_s(rez.model, strlen(model) + 1, model);
    rez.categorie = malloc(sizeof (char )* (strlen(categorie) + 1));
    strcpy_s(rez.categorie, strlen(categorie) + 1, categorie);
    rez.inchiriata = 0;
    return rez;
}

void distrugeMasina(Masina* m){
    m->nr = -1;
    free(m->model);
    free(m->categorie);
    m->model = NULL;
    m->categorie = NULL;
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
    assert(m.model == NULL);
    assert(m.categorie == NULL);
}
