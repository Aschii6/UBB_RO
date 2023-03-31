//
// Created by Daniel on 31-Mar-23.
//

#pragma once
#include "Service.h"

class Console{
private:
    Service service;
public:
    explicit Console(Service &service);
    void run();
    void adaugaCarte();
    void stergeCarte();
    void modificaCarte();
    void afiseazaCarti();
    void cautaCarte();
    void filtrareCarti();
    void sortareCarti();
};