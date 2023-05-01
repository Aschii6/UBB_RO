//
// Created by Daniel on 29-Apr-23.
//

#pragma once
#include <vector>
#include <utility>

using namespace std;

typedef int TElem;
typedef int TPrioritate;

typedef std::pair<TElem, TPrioritate> Element;

typedef bool (*Relatie)(TPrioritate p1, TPrioritate p2);

class CP {
private:
    int capacitate;
    Element* e;
    int* urm;
    Relatie rel;
    int prim, primGol;

    void asiguraCapacitate();
public:
    //constructorul implicit
    explicit CP(Relatie r);

    //adauga un element in CP
    void adauga(TElem e, TPrioritate p);

    //acceseaza elementul cel mai prioritar in raport cu relatia de ordine
    //arunca exceptie daca CP e vida
    [[nodiscard]] Element element()  const;

    //sterge elementul cel mai prioritar si il returneaza
    //arunca exceptie daca CP e vida
    Element sterge();

    //verifica daca CP e vida;
    [[nodiscard]] bool vida() const;

    // destructorul cozii
    ~CP();
};
