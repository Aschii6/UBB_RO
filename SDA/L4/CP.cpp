#include "CP.h"
#include <exception>
using namespace std;


CP::CP(Relatie r) {
    capacitate = 5;
    e = new Element[capacitate];
    urm = new int[capacitate];
    rel = r;
    prim = -1;
    primGol = 0;

    for (int i = 0; i < capacitate; ++i) {
        urm[i] = -2; // gol
    }
}

void CP::asiguraCapacitate() {
    if (primGol != -1)
        return;

    auto* newE = new Element[capacitate*2];
    auto* newUrm = new int[capacitate*2];

    for (int i = 0; i < capacitate; ++i) {
        newE[i] = e[i];
        newUrm[i] = urm[i];
    }

    for (int i = capacitate; i < capacitate*2; ++i) {
        newUrm[i] = -2;
    }

    primGol = capacitate;
    capacitate *= 2;

    delete[] e;
    delete[] urm;
    e = newE;
    urm = newUrm;
}

void CP::adauga(TElem e, TPrioritate p) {
    asiguraCapacitate();
    Element elem(e, p);

    if (prim == -1){
        prim = 0;
        this->e[0] = elem;
        urm[0] = -1;
        primGol = 1;
        return;
    }

    this->e[primGol] = elem;
    // determinam prioritatea (urm)
    int i = prim;
    int iAnt = -1;
    while (i != -1 && rel(this->e[i].second, p)){
        iAnt = i;
        i = urm[i];
    }

    if (iAnt == -1){
        urm[primGol] = prim;
        prim = primGol;
    } else{
        urm[primGol] = i; // = urm[iAnt]
        urm[iAnt] = primGol;
    }

    int fostPrimGol = primGol;
    for (int j = primGol; j < capacitate; ++j) {
        if (urm[j] == -2){
            primGol = j;
            break;
        }
    }
    if (fostPrimGol == primGol)
        primGol = -1;
}

Element CP::element() const {
    if (vida())
        throw exception();
    Element elem = e[prim];
    return {pair<TElem, TPrioritate>(elem.first, elem.second)};
}

Element CP::sterge() {
    if (vida())
        throw exception();
    Element elem = e[prim];

    if (urm[prim] == -1){
        prim = -1;
        primGol = 0;
    } else{
        prim = urm[prim];
        if (primGol > prim)
            primGol = prim;
    }

    return {pair<TElem, TPrioritate>(elem.first, elem.second)};
}

bool CP::vida() const {
    if (prim == -1)
        return true;
    return false;
}


CP::~CP() {
    delete[] e;
    delete[] urm;
};