//
// Created by Daniel on 09-Mar-23.
//

#include "Masina.h"
#include <string.h>
#include <assert.h>
#include <stdlib.h>

Masina creeazaMasina(int nr, char* model, char* categorie){
    Masina rez;
    rez.nr = nr;
    rez.model = malloc(sizeof(char)* (strlen(model) + 1));
//    strcpy_s(rez.model, strlen(model) + 1, model);
    strcpy(rez.model, model);
    rez.categorie = malloc(sizeof (char )* (strlen(categorie) + 1));
//    strcpy_s(rez.categorie, strlen(categorie) + 1, categorie);
    strcpy(rez.categorie, categorie);
    rez.inchiriata = 0;
    return rez;
}

//Masina creeazaMasina(int nr, char* model, char* categorie){
//    Masina rez;
//    rez.nr = nr;
//    strcpy_s(rez.model, strlen(model) + 1, model);
//    strcpy_s(rez.categorie, strlen(categorie) + 1, categorie);
//    rez.inchiriata = 0;
//    return rez;
//}

void distrugeMasina(Masina* m){
    free(m->model);
    free(m->categorie);
    m->model = NULL;
    m->categorie = NULL;
    m->nr = 0;
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

Masina copieMasina(Masina* m){
    return creeazaMasina(m->nr, m->model, m->categorie);
}

void testeCreeazaDistruge(){
    Masina m = creeazaMasina(520, "KIA", "SUV");
    assert(m.nr == 520);
    assert(strcmp(m.model, "KIA") == 0);
    assert(strcmp(m.categorie, "SUV") == 0);

    distrugeMasina(&m);
    assert(m.nr == 0);
    assert(m.model == NULL);
    assert(m.categorie == NULL);
}

void testeCopieMasina(){
    Masina m = creeazaMasina(520, "KIA", "SUV");
    Masina copie = copieMasina(&m);
    assert(m.nr == copie.nr);
    assert(strcmp(m.model, copie.model) == 0);
    assert(strcmp(m.categorie, copie.categorie) == 0);
    distrugeMasina(&m);
    distrugeMasina(&copie);
}
