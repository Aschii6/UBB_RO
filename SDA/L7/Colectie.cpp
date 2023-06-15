#include "Colectie.h"
#include "IteratorColectie.h"
#include <exception>
#include <algorithm>

using std::make_pair;

// teta(n)
Colectie::Colectie() {
    capacitate = 10;
    dimensiune = 0;
    elemente = new pereche[capacitate];
    stanga = new int[capacitate];
    dreapta = new int[capacitate];
    primLiber = 0;
    radacina = -1;

    for (int i = 0; i < capacitate; ++i) {
        stanga[i] = -2;
        dreapta[i] = -2;
    }
}

// teta(n)
void Colectie::asiguraCapacitate() {
    if (primLiber == -1) {
        auto* elementeNou = new pereche[capacitate*2];
        int* stangaNou = new int[capacitate*2];
        int* dreaptaNou = new int[capacitate*2];

        for (int i = 0; i < capacitate*2; ++i) {
            stangaNou[i] = -2;
            dreaptaNou[i] = -2;
        }

        for (int i = 0; i < capacitate; ++i) {
            elementeNou[i] = elemente[i];
            stangaNou[i] = stanga[i];
            dreaptaNou[i] = dreapta[i];
        }

        primLiber = capacitate;
        capacitate *= 2;

        delete[] elemente;
        delete[] stanga;
        delete[] dreapta;

        elemente = elementeNou;
        stanga = stangaNou;
        dreapta = dreaptaNou;
    }
}

// O(n)
int Colectie::cautaPrimLiber() {
    for (int i = primLiber; i < capacitate; ++i)
        if (stanga[i] == -2 && dreapta[i] == -2)
            return i;
    return -1;
}


// O(log2n)
void Colectie::adauga(TElem elem) {
	asiguraCapacitate();

    if (radacina == -1){
        radacina = primLiber;
        elemente[primLiber] = make_pair(elem, 1);
        stanga[primLiber] = -1;
        dreapta[primLiber] = -1;
    }
    else {
        int parinte = -1;
        int nod = radacina;
        while (true){
            if (nod == -1) {
                elemente[primLiber] = make_pair(elem, 1);
                stanga[primLiber] = -1;
                dreapta[primLiber] = -1;
                if (elem < elemente[parinte].first)
                    stanga[parinte] = primLiber;
                else
                    dreapta[parinte] = primLiber;
                break;
            }

            if (elemente[nod].first == elem){
                elemente[nod].second++;
                break;
            }

            if (elem < elemente[nod].first){
                parinte = nod;
                nod = stanga[nod];
            }
            else{
                parinte = nod;
                nod = dreapta[nod];
            }
        }
    }

    dimensiune++;
    primLiber = cautaPrimLiber();
}


// O(log2n)
bool Colectie::sterge(TElem e) {
    int nod = radacina;
    int parinte = -1;

    while (true) {
        if (nod == -1)
            return false;

        if (elemente[nod].first == e) {
            if (elemente[nod].second > 1) {
                elemente[nod].second--;
                dimensiune--;
                return true;
            } else {
                if (stanga[nod] == -1 && dreapta[nod] == -1) {
                    if (parinte == -1) {
                        radacina = -1;
                        if (primLiber > nod)
                            primLiber = nod;
                        dimensiune--;
                        return true;
                    }
                    if (elemente[parinte].first > elemente[nod].first)
                        stanga[parinte] = -1;
                    else
                        dreapta[parinte] = -1;
                    if (primLiber > nod)
                        primLiber = nod;
                    dimensiune--;
                    return true;
                }
                if (stanga[nod] == -1) {
                    if (parinte == -1) {
                        radacina = dreapta[nod];
                        if (primLiber > nod)
                            primLiber = nod;
                        dimensiune--;
                        return true;
                    }
                    if (elemente[parinte].first > elemente[nod].first)
                        stanga[parinte] = dreapta[nod];
                    else
                        dreapta[parinte] = dreapta[nod];
                    if (primLiber > nod)
                        primLiber = nod;
                    dimensiune--;
                    return true;
                }
                if (dreapta[nod] == -1) {
                    if (parinte == -1) {
                        radacina = stanga[nod];
                        if (primLiber > nod)
                            primLiber = nod;
                        dimensiune--;
                        return true;
                    }
                    if (elemente[parinte].first > elemente[nod].first)
                        stanga[parinte] = stanga[nod];
                    else
                        dreapta[parinte] = stanga[nod];
                    if (primLiber > nod)
                        primLiber = nod;
                    dimensiune--;
                    return true;
                }
                if (stanga[nod] != -1 && dreapta[nod] != -1) {
                    int succesor = nodSuccessor(e, nod);
                    if (parinte == -1) {
                        radacina = succesor;
                        if (primLiber > nod)
                            primLiber = nod;
                        dimensiune--;
                        return true;
                    }
                    if (elemente[parinte].first > elemente[nod].first)
                        stanga[parinte] = succesor;
                    else
                        dreapta[parinte] = succesor;
                    if (primLiber > nod)
                        primLiber = nod;
                    dimensiune--;
                    return true;
                }
            }
        }

        if (e < elemente[nod].first){
            parinte = nod;
            nod = stanga[nod];
        }
        else{
            parinte = nod;
            nod = dreapta[nod];
        }
    }
}


// teta(log2n)
int Colectie::nodSuccessor(TElem elem, int nod) const {
    int st = stanga[nod];
    int dr = dreapta[nod];

    if (st == -1 && dr == -1)
        return nod;

    if (st == -1)
        return nodSuccessor(elem, dr);

    if (dr == -1)
        return nodSuccessor(elem, st);

    return functieNumar(nodSuccessor(elem, st), nodSuccessor(elem, dr), nod);
}


// teta(1)
int Colectie::functieNumar(int a, int b, int nod) const {
    if (a < nod && b < nod)
        return -1;
    if (a < nod && b > nod)
        return b;
    if (a > nod && b < nod)
        return a;

    return std::min(a, b);
}


// O(log2n)
bool Colectie::cautaRecursiv(TElem elem, int nod) const {
    if (nod == -1)
        return false;
    if (elemente[nod].first == elem)
        return true;
    if (elem < elemente[nod].first)
        return cautaRecursiv(elem, stanga[nod]);
    else
        return cautaRecursiv(elem, dreapta[nod]);
}


// O(log2n)
bool Colectie::cauta(TElem elem) const {
    return cautaRecursiv(elem, radacina);
}


// O(log2n)
int Colectie::nrAparitii(TElem elem) const {
	int nod = radacina;

    while (true){
        if (nod == -1)
            return 0;

        if (elemente[nod].first == elem)
            return elemente[nod].second;

        if (elem < elemente[nod].first)
            nod = stanga[nod];
        else
            nod = dreapta[nod];
    }
}


// teta(1)
int Colectie::dim() const {
	return dimensiune;
}


// teta(1)
bool Colectie::vida() const {
	if (dimensiune == 0)
	    return true;
    return false;
}


// teta(1)
IteratorColectie Colectie::iterator() const {
	return {IteratorColectie(*this)};
}


// teta(1)
Colectie::~Colectie() {
    delete[] elemente;
    delete[] stanga;
    delete[] dreapta;
}