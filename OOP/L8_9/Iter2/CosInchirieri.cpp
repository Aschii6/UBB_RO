//
// Created by Daniel on 20-Apr-23.
//

#include "CosInchirieri.h"

void CosInchirieri::add(Carte &carte) {
    cos.push_back(carte);
}

void CosInchirieri::clear() {
    cos.clear();
}

vector<Carte> CosInchirieri::getAll() const {
    return cos;
}

void CosInchirieri::addRandom(int nr, vector<Carte> carti) {
    std::shuffle(carti.begin(), carti.end(), std::default_random_engine(std::random_device{}()));
    while (cos.size() < nr && !carti.empty()){
        cos.push_back(carti.back());
        carti.pop_back();
    }
}

int CosInchirieri::dimensiune() {
    return cos.size();
}
