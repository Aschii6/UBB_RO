#include <malloc.h>
#include "repo_cheltuiala.h"

Lista_cheltuieli* constructor_lista_repo(destroy_function destr_fct){
    Lista_cheltuieli* lista = malloc(sizeof(Lista_cheltuieli));
    lista->cp = 2;
    lista->dimensiune = 0;
    lista->cheltuieli = (ElemType*)malloc(lista->cp * sizeof(Cheltuiala));
    lista->destr_fct = destr_fct;
    return lista;
}

void destructor_lista_repo(Lista_cheltuieli *lista){
    for (int i = 0; i < lista->dimensiune; i++){
        lista->destr_fct(lista->cheltuieli[i]);
    }
    free(lista->cheltuieli);
    free(lista);
}

int adauga_cheltuiala_repo(Lista_cheltuieli *lista, ElemType c){
    int dim = lista->dimensiune;
    if (dim == lista->cp)
        redimensionare(lista);
    lista->cheltuieli[dim] = c;
    lista->dimensiune++;
    return 0;
}

int sterge_cheltuiala_dupa_id_repo(Lista_cheltuieli *lista, int id){
    for (int i = 0; i < lista->dimensiune; i++)
    {
        Cheltuiala *c = lista->cheltuieli[i];
        if (c->id == id) {
            Cheltuiala* de_sters = lista->cheltuieli[i];
            for (int j = i; j < lista->dimensiune - 1; j++)
                lista->cheltuieli[j] = lista->cheltuieli[j + 1];
            lista->dimensiune--;
            destructor_cheltuiala(de_sters);
            return 0;
        }
    }
    return -1;
}

int modifica_cheltuiala_repo(Lista_cheltuieli* lista, Cheltuiala c_noua){
    for (int i = 0; i < lista->dimensiune; i++){
        if (is_equal_by_id(lista->cheltuieli[i], &c_noua)){
            lista->destr_fct(lista->cheltuieli[i]);
            lista->cheltuieli[i] = copy_cheltuiala(&c_noua);
            return 0;
        }
    }
    return -1;
}

int lungime_lista_repo(Lista_cheltuieli lista){
    return lista.dimensiune;
}

void redimensionare(Lista_cheltuieli *lista) {
    ElemType *lista_noua = malloc(2 * lista->cp * sizeof(Cheltuiala));
    for (int i = 0; i < lista->dimensiune; i++)
        lista_noua[i] = lista->cheltuieli[i];
    free(lista->cheltuieli);
    lista->cheltuieli = lista_noua;
    lista->cp = 2 * lista->cp;
}

Lista_cheltuieli* copie_lista(Lista_cheltuieli* lista){
    Lista_cheltuieli* copie = constructor_lista_repo((destroy_function) destructor_cheltuiala);
    for (int i = 0; i < lista->dimensiune; i++){
        ElemType el = lista->cheltuieli[i];
        ElemType el_copie = copy_cheltuiala(el);
        adauga_cheltuiala_repo(copie, el_copie);
    }
    return copie;
}
