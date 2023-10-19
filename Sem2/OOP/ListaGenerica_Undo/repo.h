#pragma once

typedef void* ElemType;

typedef void (*destroy_function)(ElemType);

typedef struct {
    int cp;
    int dimensiune;
    ElemType* cheltuieli;
    destroy_function destr_fct;
}Lista_cheltuieli;

/***
 * Creeaza o lista de cheltuieli
 * @return lista_cheltuieli
 */
Lista_cheltuieli* constructor_lista_repo(destroy_function dest_fct);

/***
 * Dealoca lista dinamica (destructor) si toate cheltuielile sale
 * @param lista Lista_cheltuieli
 */
void destructor_lista_repo(Lista_cheltuieli *lista);

/***
 * Adauga o cheltuiala intr-o lista de cheltuieli
 * @param lista Lista_cheltuieli
 * @param c Cheltuiala
 * @return -> -1 daca exista deja cheltuiala c
 *         -> 0 daca cheltuiala c a fost adaugata cu succes
 */
int adauga_cheltuiala_repo(Lista_cheltuieli *lista, ElemType c);

/***
 * Sterge o cheltuiala care are id-ul dat
 * @param lista Lista_cheltuieli
 * @param id int
 * @return -> -1 daca nu a fost gasit id-ul
 *         -> 0 daca stergerea a fost efectuata cu succes
 */
int sterge_cheltuiala_dupa_id_repo(Lista_cheltuieli *lista, int id);

/***
 * Modifica datele unei chletuieli date
 * @param lista Lista_cheltuieli
 * @param c_noua Cheltuiala
 * @return -> -1 daca nu a fost gasita cheltuiala
 *         -> 0 daca a fost modificata cu succes
 */
int modifica_cheltuiala_repo(Lista_cheltuieli *lista, Cheltuiala c_noua);

/***
 * Returneaza lungimea listei de cheltuieli
 * @param lista Lista_cheltuieli
 * @return lungime lista
 */
int lungime_lista_repo(Lista_cheltuieli lista);

/***
 * Redimensionare lista alocata dinamic
 * Aloca dinamic o noua lista
 * Copiaza elementele din lista initiala in cea noua
 * Dealocare lista initiala
 * Redenumirea listei
 * Dublarea capacitatii
 * @param lista Lista_cheltuieli
 */
void redimensionare(Lista_cheltuieli *lista);

/*
 * Returneaza o copie a listei
 */
Lista_cheltuieli* copie_lista(Lista_cheltuieli* lista);
