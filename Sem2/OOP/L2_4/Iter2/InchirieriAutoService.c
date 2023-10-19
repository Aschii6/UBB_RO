//
// Created by Daniel on 12-Mar-23.
//

#include "InchirieriAutoService.h"
#include "FunctieSort.h"
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
    //distrugeMasina(&m);
    return 0;
}

int modificaMasina(Lista* l, int nr, char* model, char* categorie){
    Masina m = creeazaMasina(nr, model, categorie);
    int valid = valideaza(m);
    if (valid != 0 ){
        distrugeMasina(&m);
        return valid;
    }
    for (int i = 0; i < dimensiune(l); ++i) {
        if (elem(l, i).nr == nr){
            Masina mDeSters = modifica(l, i, m);
            distrugeMasina(&mDeSters);
        }
    }
    return 0;
}

Lista filtreazaModel(Lista* l, char* modelSubstring){
    if (modelSubstring == NULL || strlen(modelSubstring) == 0)
        return copieLista(l);

    Lista rez = creeazaGoala();
    for (int i = 0; i < dimensiune(l); ++i) {
        Masina m = elem(l, i);
        if (strstr(m.model, modelSubstring) != NULL)
            adauga(&rez, copieMasina(&m));
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
            adauga(&rez, copieMasina(&m));
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
    int eroare;
    eroare = adaugaMasina(&l, -3, "a", "c");
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

    Lista filtrat1;
    filtrat1 = filtreazaCategorie(&l, NULL);
    assert(dimensiune(&filtrat1) == 2);
    distrugeLista(&filtrat1);

    Lista filtrat2;
    filtrat2 = filtreazaModel(&l, NULL);
    assert(dimensiune(&filtrat2) == 2);
    distrugeLista(&filtrat2);

    Lista filtrat3;
    filtrat3 = filtreazaCategorie(&l, "Altceva");
    ElemType el = elem(&filtrat3, 0);
    assert(el.nr == 5);
    distrugeLista(&filtrat3);

    Lista filtrat4;
    filtrat4 = filtreazaModel(&l, "Aici");
    el = elem(&filtrat4, 0);
    assert(el.nr == 12);
    distrugeLista(&filtrat4);

    distrugeLista(&l);
}

void testeModificaMasina(){
    Lista l = creeazaGoala();
    adaugaMasina(&l, 5, "Ceva", "Altceva");
    modificaMasina(&l, 5, "Acum", "Atunci");

    ElemType el = elem(&l, 0);
    assert(strcmp(el.model, "Acum") == 0);
    assert(strcmp(el.categorie, "Atunci") == 0);

    assert(modificaMasina(&l, -3, "a", "b") == 1);

    distrugeLista(&l);
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

    distrugeLista(&l);
}

void testeSortare(){
    Lista l = creeazaGoala();
    adaugaMasina(&l, 5, "Ceva", "Altceva");
    adaugaMasina(&l, 12, "Aici", "Acolo");
    adaugaMasina(&l, 3, "A", "B");

    Lista sortata1 = sortModel(&l);
    ElemType el = elem(&sortata1, 0);
    assert(el.nr == 3);
    distrugeLista(&sortata1);

    Lista sortata2 = sortCategorie(&l);
    el = elem(&sortata2, 0);
    assert(el.nr == 12);
    distrugeLista(&sortata2);

    distrugeLista(&l);
}
