//
// Created by Daniel on 20-Apr-23.
//

#pragma once
#include "Carte.h"
#include <vector>
#include <chrono>
#include <random>
#include <algorithm>
using std::vector;

class CosInchirieri{
private:
    vector<Carte> cos;
public:
    CosInchirieri() = default;

    void add(Carte &carte);

    void clear();

    void addRandom(int nr, vector<Carte> carti);

    [[nodiscard]] vector<Carte> getAll() const;

    int dimensiune();

    ~CosInchirieri() = default;
};