#include <exception>
#include "LI.h"
#include "IteratorLI.h"
using std::exception;

Nod::Nod(TElem e, PNod u) {
    this->elem = e;
    this->urm = u;
}

TElem Nod::getElem() const {
    return this->elem;
}

PNod Nod::getUrm() const {
    return this->urm;
}

LI::LI() {
    this->prim = nullptr;
    this->dimensiune = 0;
    this->ultim = nullptr;
}

int LI::dim() const {
	return this->dimensiune;
}

bool LI::vida() const {
    if (this->dimensiune == 0)
        return true;
    return false;
}

TElem LI::element(int i) const {
    if (i < 0 || i >= this->dimensiune)
        throw exception();
    int j = 0;
    PNod p = this->prim;
    while (j < i){
        p = p->urm;
        j++;
    }
    return p->elem;
}

TElem LI::modifica(int i, TElem e) {
    if (i < 0 || i >= this->dimensiune)
        throw exception();
    PNod p = this->prim;
    int j = 0;
    while (j < i) {
        p = p->urm;
        j++;
    }
    TElem rez = p->elem;
    p->elem = e;
    return rez;
}

void LI::adaugaSfarsit(TElem e) {
    PNod p = new Nod(e, nullptr);
    if (this->prim == nullptr){
        this->prim = p;
        this->ultim = p;
    } else {
        this->ultim->urm = p;
        this->ultim = p;
    }
    this->dimensiune++;
}

void LI::adauga(int i, TElem e) {
    if (i < 0 || i > this->dimensiune)
        throw exception();
    if (i == this->dimensiune)
        adaugaSfarsit(e);
    else if (i == 0){
        PNod p = new Nod(e, this->prim);
        this->prim = p;
        this->dimensiune++;
    } else {
        PNod p = this->prim;
        int j = 0;
        while (j < i - 1) {
            p = p->urm;
            j++;
        }
        PNod nou = new Nod(e, p->urm);
        p->urm = nou;
        this->dimensiune++;
    }
}

TElem LI::sterge(int i) {
    if (i < 0 || i >= dimensiune) {
        throw exception();
    }
    PNod p;
    if (i == 0) {
        p = prim;
        prim = prim->urm;
        if (prim == nullptr) {
            ultim = nullptr;
        }
    } else {
        PNod q = prim;
        int j = 0;
        while (j < i - 1) {
            q = q->urm;
            j++;
        }
        p = q->urm;
        q->urm = p->urm;
        if (ultim == p) {
            ultim = q;
        }
    }
    TElem rez = p->elem;
    delete p;
    dimensiune--;
    return rez;
}

int LI::cauta(TElem e) const{
    PNod p = this->prim;
    while (p != nullptr){
        if (p->elem == e)
            return e;
        p = p->urm;
    }
    return -1;
}

IteratorLI LI::iterator() const {
    return {IteratorLI(*this)};
}

LI::~LI() {
    while (prim != nullptr){
        PNod p = prim;
        prim = prim->urm;
        delete p;
    }
}
