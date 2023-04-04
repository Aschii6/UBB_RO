#include "IteratorLI.h"
#include "LI.h"
#include <exception>
using std::exception;

IteratorLI::IteratorLI(const LI& li): lista(li) {
    curent = lista.prim;
}

void IteratorLI::prim(){
 	curent = lista.prim;
}

void IteratorLI::urmator(){
    if (!valid())
        throw exception();
 	curent = curent->getUrm();
}

bool IteratorLI::valid() const{
    return curent != nullptr;
}

TElem IteratorLI::element() const{
    if (!valid())
        throw exception();
    return curent->getElem();
}
