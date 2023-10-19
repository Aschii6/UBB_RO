#include "Iterator.h"
#include "DO.h"

#include <exception>
using namespace std;

DO::DO(Relatie r) {
    dimensiune = 0;
    capacitate = 13;
    elems = new TElem[capacitate];
    urm = new int[capacitate];
    primLiber = 0;
    rel = r;

    for (int i = 0; i < capacitate; i++) {
        urm[i] = -1;
        elems[i] = make_pair(-1, NULL_TVALOARE);
    }
}

//adauga o pereche (cheie, valoare) in dictionar
//daca exista deja cheia in dictionar, inlocuieste valoarea asociata cheii si returneaza vechea valoare
//daca nu exista cheia, adauga perechea si returneaza null
TValoare DO::adauga(TCheie c, TValoare v) {
    asiguraDimensiune();

    int hcode = hashFunction(c);
    if (elems[hcode].first == -1){
        elems[hcode] = make_pair(c, v);
        dimensiune++;

        if (primLiber == hcode)
            for (int i = 0; i < capacitate; ++i)
                if (elems[i].first == -1){
                    primLiber = i;
                    break;
                }

        return NULL_TVALOARE;
    }

    int last;
    int i = hcode;
    while (i != -1){
        if (elems[i].first == c){
            TValoare old = elems[i].second;
            elems[i].second = v;
            return old;
        }
        last = i;
        i = urm[i];
    }

    elems[primLiber] = make_pair(c, v);
    urm[last] = primLiber;
    dimensiune++;

    for (int j = 0; j < capacitate; ++j) {
        if (elems[j].first == -1){
            primLiber = j;
            break;
        }
    }
    return NULL_TVALOARE;
}

//cauta o cheie si returneaza valoarea asociata (daca dictionarul contine cheia) sau null
TValoare DO::cauta(TCheie c) const {
    int i = hashFunction(c);

    while (i != -1){
        if (elems[i].first == c)
            return elems[i].second;
        i = urm[i];
    }
    return NULL_TVALOARE;
}

//sterge o cheie si returneaza valoarea asociata (daca exista) sau null
TValoare DO::sterge(TCheie c) {
    int i = hashFunction(c);
//    int j = -1;
    int k = 0;

    while (k < capacitate && urm[k] != -1){
        k++;
    }

//    if (k < capacitate)
//        j = k;

    while (i != -1 && elems[i].first != c){
//        j = i;
        i = urm[i];
    }

    if (i == -1)
        return NULL_TVALOARE;
    else{
        TValoare old = elems[i].second;
        bool gata = false;
        do {
            int p = urm[i];
            int pp = i;
            while (p != -1 && hashFunction(elems[p].first) != i){
                pp = p;
                p = urm[p];
            }
            if (p == -1){
                urm[pp] = -1;
                gata = true;
            }
            else{
                elems[i] = elems[p];
                i = p;
//                j = pp;
            }
        } while (!gata);
        dimensiune--;
        elems[i] = make_pair(-1, NULL_TVALOARE);
        return old;
    }
}

//returneaza numarul de perechi (cheie, valoare) din dictionar
int DO::dim() const {
    return dimensiune;
}

//verifica daca dictionarul e vid
bool DO::vid() const {
    if (dimensiune == 0)
        return true;
    return false;
}

Iterator DO::iterator() const {
    return {Iterator(*this)};
}

DO::~DO() {
    delete[] elems;
    delete[] urm;
}

bool DO::factorAtins() const {
    float factor = (float) dimensiune / (float) capacitate;
    return factor >= 0.75;
}

int DO::hashFunction(TCheie c) const {
    if (c < 0)
        c *= -1;
    return c % capacitate;
}

void DO::asiguraDimensiune() {
    if (!factorAtins())
        return;

    int oldCap = capacitate;
    capacitate *= 2;
    primLiber = 0;

    auto* nouElems = new TElem[capacitate];
    auto* nouUrm = new int[capacitate];

    for (int i = 0; i < capacitate; i++) {
        nouElems[i] = make_pair(-1, NULL_TVALOARE);
        nouUrm[i] = -1;
    }

    for (int i = 0; i < oldCap; ++i) {
        if (elems[i].first == -1)
            continue;

        int c = elems[i].first;
        int hcode = hashFunction(c);

        if (nouElems[hcode].first == -1) {
            nouElems[hcode] = elems[i];

            if (primLiber == hcode)
                for (int j = primLiber + 1; j < capacitate; ++j)
                    if (nouElems[j].first == -1){
                        primLiber = j;
                        break;
                    }

            continue;
        }

        int j = hcode;

        while (nouUrm[j] != -1) {
            j = nouUrm[j];
        }

        nouElems[primLiber] = elems[i];
        nouUrm[j] = primLiber;

        for (int k = primLiber; k < capacitate; ++k) {
            if (nouElems[k].first == -1){
                primLiber = k;
                break;
            }
        }
    }

    delete[] elems;
    delete[] urm;
    elems = nouElems;
    urm = nouUrm;
}