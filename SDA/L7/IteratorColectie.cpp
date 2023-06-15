#include "IteratorColectie.h"
#include "Colectie.h"
#include <exception>

// O(n)
IteratorColectie::IteratorColectie(const Colectie& c): col(c) {
    curent = 0;
    pozitie = 1;

    while (col.stanga[curent] == -2 && c.dreapta[curent] == -2)
        curent++;
    primCurent = curent;
}

// O(n)
void IteratorColectie::prim() {
    curent = primCurent;
    pozitie = 1;
}

// O(n)
void IteratorColectie::urmator() {
    if (!valid())
        throw std::exception();

    pozitie++;
    if (pozitie > col.elemente[curent].second) {
        curent++;
        while (col.stanga[curent] == -2 && col.dreapta[curent] == -2 && curent < col.capacitate)
            curent++;

        pozitie = 1;
    }
}

// teta(1)
bool IteratorColectie::valid() const {
    if (curent < col.capacitate)
        return true;
	return false;
}

// teta(1)
TElem IteratorColectie::element() const {
    if (!valid())
        throw std::exception();
    return col.elemente[curent].first;
}

// O(n)
void IteratorColectie::anterior() {
    pozitie--;
    if (pozitie == 0) {
        if (curent == primCurent)
            throw std::exception();
        curent--;
        while (col.stanga[curent] == -2 && col.dreapta[curent] == -2)
            curent--;
        pozitie = col.elemente[curent].second;
    }
}