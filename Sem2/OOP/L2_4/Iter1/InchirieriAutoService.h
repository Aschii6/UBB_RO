//
// Created by Daniel on 12-Mar-23.
//

#pragma once
#include "Lista.h"

/*
 * adauga o masina in baza de date a serviciului inchirieri auto
 */
int adaugaMasina(Lista* l, int nr, char* model, char* categorie);

/*
 * modifica modelul si categegoria unei masini daca aceasta exista
 */
int modificaMasina(Lista* l, int nr, char* model, char* categorie);

/*
 * filtreaza masinile dupa model
 * modelSubstring - cstring
 * returneaza o lista cu toate masinile care contin substringul dat in model
 */
Lista filtreazaModel(Lista* l, char* modelSubstring);

/*
 * filtreaza masinile dupa categorie
 * categorieSubstring - cstring
 * returneaza o lista cu toate masinile care contin substringul dat in categorie
 */
Lista filtreazaCategorie(Lista* l, char* categorieSubstring);

/*
 * inchiriaza o masina daca exista si nu este inchiriata deja
 */
int inchiriazaMasina(Lista* l, int nr);

/*
 * retruneaza o masina daca aceasta este inchiriata
 */
int returneazaMasina(Lista* l, int nr);

void testeAdaugaMasina();

void testeModificaMainsa();

void testeInchiriazaReturneaza();
