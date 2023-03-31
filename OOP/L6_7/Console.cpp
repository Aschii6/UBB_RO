//
// Created by Daniel on 31-Mar-23.
//

#include "Console.h"
#include "iostream"
using std::cin;
using std::cout;

Console::Console(Service &service) {
    this->service = service;
}


void Console::run(){
    cout << "1.Adauga Carte\n2.Modifica Carte\n3.Sterge Carte\n4.Afiseaza Carti\n5.Cauta Carte\n6.Filtrare Carti\n7.Sortare Carti\n0.Exit\n";
}
