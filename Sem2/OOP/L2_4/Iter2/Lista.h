//
// Created by Daniel on 12-Mar-23.
//

#pragma once
#include "Masina.h"
typedef Masina ElemType;

typedef struct {
    ElemType* elems;
    int lg;
    int cap;
} Lista;

/*
 * creeaza o lista goala
 */
Lista creeazaGoala();

/*
 * distruge lista
 */
void distrugeLista(Lista* l);

/*
 * acceseaza un element din lista
 * poz - pozitia elementului, trebuie sa fie valida
 * returneaza elementul de pe pozitia data din lista
 */
ElemType elem(Lista* l, int poz);

/*
 * returneaza nr de elem din lista
 */
int dimensiune(Lista* l);

/*
 * asigura ca lista are destula capacitate ptr a adauga un element
 */
void asiguraCapacitate(Lista* l);

/*
 * adauga un element in lista
 * post: acesta este adaugat la finalul listei
 */
void adauga(Lista* l, ElemType el);

/*
 * modifica un element din lista
 */
Masina modifica(Lista* l, int poz, ElemType el);

/*
 * returneaza o copie a listei
 */
Lista copieLista(Lista* l);

/*
 * inchiriaza o masina daca exista si nu este inchiriata deja
 */
int inchiriaza(Lista* l, int nr);

/*
 * retruneaza o masina daca aceasta este inchiriata
 */
int returneaza(Lista* l, int nr);

void testCreeazaLista();
void testIteratorLista();
void testCopieLista();
