//
// Created by Daniel on 10-Mar-23.
//

#pragma once

typedef struct {
    int nr;
    char* model;
    char* categorie;
    int inchiriata;
} Masina;

/*
 * Creeaza o noua masina
 */
Masina creeazaMasina(int nr, char* model, char* categorie);

Masina copieMasina(Masina* m);

/*
 * Dealocheaza memoria alocata unei masini
 */
void distrugeMasina(Masina* masina);

/*
 * Verifica daca o masina este valida
 * Returneaza 0 daca da; 1 - nr invaid ; 2 - model invalid ; 3 - categorie invalida
 */
int valideaza(Masina);

void testeCreeazaDistruge();