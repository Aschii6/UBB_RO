//
// Created by Daniel on 31-Mar-23.
//

#pragma once
#include <vector>
using std::vector;
#include "Carte.h"

class Repo{
private:
    std::vector<Carte> carti;

public:
    Repo() = default;

    void addCarte(const Carte& carte){
        carti.push_back(carte);
    }

    vector<Carte> getAll(){
        return carti;
    }

//    void modifyCarte(const Carte& carte){
//
//    }

    ~Repo() = default;
};
