#include "Iterator.h"
#include "DO.h"
#include <algorithm>

using namespace std;


Iterator::Iterator(const DO& d) : dict(d){
    for (int i = 0; i < dict.dim(); i++) {
        if (dict.elems[i].second != NULL_TVALOARE) {
            lista.push_back(dict.elems[i]);
        }
    }
    for (int i = 0; i < lista.size(); i++) {
        for (int j = i + 1; j < lista.size(); j++) {
            if (!dict.rel(lista[i].first, lista[j].first)) {
                swap(lista[i], lista[j]);
            }
        }
    }

    curent = 0;
}


void Iterator::prim(){
	curent = 0;
}


void Iterator::urmator(){
//	if (!valid())
//        throw exception();
    curent++;
}

bool Iterator::valid() const{
	if (curent < lista.size())
        return true;
	return false;
}

TElem Iterator::element() const{
	return make_pair(lista[curent].first, lista[curent].second);
}
