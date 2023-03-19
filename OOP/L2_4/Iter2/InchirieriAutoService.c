//
// Created by Daniel on 12-Mar-23.
//

#include "InchirieriAutoService.h"
#include "FunctieSort.h"
#include <stdlib.h>
#include <string.h>
#include <assert.h>

int adaugaMasina(Lista* l, int nr, char* model, char* categorie){
    Masina m = creeazaMasina(nr, model, categorie);
    int valid = valideaza(m);
    if (valid != 0 ){
        distrugeMasina(&m);
        return valid;
    }

    adauga(l, m);
    return 0;
}

int modificaMasina(Lista* l, int nr, char* model, char* categorie){
    Masina m = creeazaMasina(nr, model, categorie);
    int valid = valideaza(m);
    if (valid != 0 )
        return valid;

    modifica(l, m);
    return 0;
}

Lista filtreazaModel(Lista* l, char* modelSubstring){
    if (modelSubstring == NULL || strlen(modelSubstring) == 0)
        return copieLista(l);

    Lista rez = creeazaGoala();
    for (int i = 0; i < dimensiune(l); ++i) {
        Masina m = elem(l, i);
        if (strstr(m.model, modelSubstring) != NULL)
            adauga(&rez, m);
    }
    return rez;
}

Lista filtreazaCategorie(Lista* l, char* categorieSubstring){
    if (categorieSubstring == NULL || strlen(categorieSubstring) == 0)
        return copieLista(l);

    Lista rez = creeazaGoala();
    for (int i = 0; i < dimensiune(l); ++i) {
        Masina m = elem(l, i);
        if (strstr(m.categorie, categorieSubstring) != NULL)
            adauga(&rez, m);
    }
    return rez;
}

int cmpModel(Masina* m1, Masina* m2){
    return strcmp(m1->model, m2->model);
}

Lista sortModel(Lista* l){
    Lista copieL = copieLista(l);
    sort(&copieL, cmpModel);
    return copieL;
}

int cmpCategorie(Masina* m1, Masina* m2){
    return strcmp(m1->categorie, m2->categorie);
}

Lista sortCategorie(Lista* l){
    Lista copieL = copieLista(l);
    sort(&copieL, cmpCategorie);
    return copieL;
}

int inchiriazaMasina(Lista* l, int nr){
    int eroare = inchiriaza(l, nr);
    if (eroare != 0)
        return eroare;
    return 0;
}

int returneazaMasina(Lista* l, int nr){
    int eroare = returneaza(l, nr);
    if (eroare != 0)
        return eroare;
    return 0;
}

void testeAdaugaMasina() {
    Lista l = creeazaGoala();

    int eroare = adaugaMasina(&l, -3, "a", "c");
    assert(eroare == 1);
    assert(dimensiune(&l) == 0);

    eroare = adaugaMasina(&l, 5, "", "c");
    assert(eroare == 2);
    assert(dimensiune(&l) == 0);

    eroare = adaugaMasina(&l, 5, "Model", "");
    assert(eroare == 3);
    assert(dimensiune(&l) == 0);

    adaugaMasina(&l, 5, "Ceva", "Altceva");
    adaugaMasina(&l, 12, "Aici", "Acolo");

    Lista filtrat = filtreazaCategorie(&l, NULL);
    assert(dimensiune(&filtrat) == 2);

    filtrat = filtreazaModel(&l, NULL);
    assert(dimensiune(&filtrat) == 2);

    filtrat = filtreazaCategorie(&l, "Altceva");
    ElemType el = elem(&filtrat, 0);
    assert(el.nr == 5);

    filtrat = filtreazaModel(&l, "Aici");
    el = elem(&filtrat, 0);
    assert(el.nr == 12);
}

void testeModificaMainsa(){
    Lista l = creeazaGoala();
    adaugaMasina(&l, 5, "Ceva", "Altceva");
    modificaMasina(&l, 5, "Acum", "Atunci");

    ElemType el = elem(&l, 0);
    assert(strcmp(el.model, "Acum") == 0);
    assert(strcmp(el.categorie, "Atunci") == 0);

    modificaMasina(&l, -3, "a", "b");
}

void testeInchiriazaReturneaza(){
    Lista l = creeazaGoala();
    adauga(&l, creeazaMasina(5, "Ferrari", "Supercar"));
    inchiriazaMasina(&l, 5);
    ElemType el = elem(&l, 0);
    assert(el.inchiriata == 1);
    returneazaMasina(&l, 5);
    el = elem(&l, 0);
    assert(el.inchiriata == 0);

    int eroare = inchiriazaMasina(&l, 15);
    assert(eroare == 1);

    eroare = returneazaMasina(&l, 15);
    assert(eroare == 1);

    eroare = returneazaMasina(&l, 5);
    assert(eroare == 2);

    inchiriazaMasina(&l, 5);
    eroare = inchiriazaMasina(&l, 5);
    assert(eroare == 2);
}

void testeSortare(){
Lista l = creeazaGoala();
    adaugaMasina(&l, 5, "Ceva", "Altceva");
    adaugaMasina(&l, 12, "Aici", "Acolo");
    adaugaMasina(&l, 3, "A", "B");

    Lista sortata = sortModel(&l);
    ElemType el = elem(&sortata, 0);
    assert(el.nr == 3);

    sortata = sortCategorie(&l);
    el = elem(&sortata, 0);
    assert(el.nr == 12);
}
