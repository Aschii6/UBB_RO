#include "service.h"
#include <string.h>

Service constructor_service(){
    Service rez;
    rez.lista = constructor_lista_repo((destroy_function) destructor_cheltuiala);
    rez.lista_undo = constructor_lista_repo((destroy_function) destructor_lista_repo);
    return rez;
}

void destructor_service(Service* s){
    destructor_lista_repo(s->lista);
    destructor_lista_repo(s->lista_undo);
}

int undo(Service *s){
    if (s->lista_undo->dimensiune == 0)
        return -1;
    Lista_cheltuieli *lista = s->lista_undo->cheltuieli[s->lista_undo->dimensiune - 1];
    destructor_lista_repo(s->lista);
    s->lista = lista;
    s->lista_undo->dimensiune--;
    return 0;
}

int adauga_cheltuiala_service(Service *s, int zi, float suma, char tip[]){
    int id = s->lista->dimensiune;
    Cheltuiala* c = constructor_cheltuiala(id, zi, suma, tip);
    int eroare = valideaza(*c);
    if (eroare == 0){
        Lista_cheltuieli* to_undo = (Lista_cheltuieli *) copie_lista(s->lista);
        int cod_retur = adauga_cheltuiala_repo(s->lista, c);
        adauga_cheltuiala_repo(s->lista_undo, to_undo);
        return cod_retur;
    }
    else {
        destructor_cheltuiala(c);
        return eroare;
    }
}

int modifica_cheltuiala_service(Service *s, int id, int zi_noua, float suma_noua, char tip_nou[]){
    Cheltuiala* c_noua = constructor_cheltuiala(id, zi_noua, suma_noua, tip_nou);
    int eroare = valideaza(*c_noua);
    if (eroare == 0){
        Lista_cheltuieli *to_undo = copie_lista(s->lista);
        int cod_retur = modifica_cheltuiala_repo(s->lista, *c_noua);
        destructor_cheltuiala(c_noua);
        adauga_cheltuiala_repo(s->lista_undo, to_undo);
        return cod_retur;
    }
    else {
        destructor_cheltuiala(c_noua);
        return eroare;
    }
}

int sterge_cheltuiala_service(Service *s, int id){
    Lista_cheltuieli *to_undo = copie_lista(s->lista);
    int cod_retur = sterge_cheltuiala_dupa_id_repo(s->lista, id);
    if (cod_retur == 0){
        adauga_cheltuiala_repo(s->lista_undo, to_undo);
    }
    else
        destructor_lista_repo(to_undo);
    return cod_retur;
}
