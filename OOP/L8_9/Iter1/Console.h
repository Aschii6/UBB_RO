//
// Created by Daniel on 31-Mar-23.
//

#pragma once
#include "Service.h"

class Console{
private:
    Service& service;
public:
    explicit Console(Service &service);
    Console(const Console &ot) = delete;
    void run();
    void adaugaCarte();
    void stergeCarte();
    void modificaCarte();
    static void tiparesteLista(vector<Carte> &carti);
    void afiseazaCarti();
    void cautaCarte();
    void filtrareCarti();
    void sortareCarti();

    void adaugaCateva();

    void adaugaCos();
    void golesteCos();
    void umpleCos();
    void afiseazaCos();
    void exportCSV();
};