//
// Created by Daniel on 03-Mar-23.
//

#pragma once
typedef int TElem;

class Coada
{
private:
    /* aici reprezentarea */
    int dimensiune, front, rear;
    TElem *lista;
public:
    //constructorul implicit
    Coada();

    //adauga un element in coada
    void adauga(TElem e);

    //acceseaza elementul cel mai devreme introdus in coada
    //arunca exceptie daca coada e vida
    TElem element() const;

    //sterge elementul cel mai devreme introdus in coada si returneaza elementul sters (principiul FIFO)
    //arunca exceptie daca coada e vida
    TElem sterge();

    //verifica daca coada e vida;
    bool vida() const;

    //verifica daca coada e plina
    bool plina() const;

    //afiseaza toate elementele din coada
    void afiseaza_toate();

    // destructorul cozii
    ~Coada();
};
