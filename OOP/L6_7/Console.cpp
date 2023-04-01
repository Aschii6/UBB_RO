//
// Created by Daniel on 31-Mar-23.
//

#include "Console.h"
#include <iostream>
using std::cin;
using std::cout;

Console::Console(Service &service): service{service}{}

void Console::run(){
    int cmd;
    while (true){
        cout << "1.Adauga Carte\n2.Modifica Carte\n3.Sterge Carte\n4.Afiseaza Carti\n5.Cauta Carte\n6.Filtrare Carti\n7.Sortare Carti\n0.Exit\n";
        cin >> cmd;
        try {
            switch (cmd){
                case 1:
                    adaugaCarte();
                    break;
                case 2:
                    modificaCarte();
                    break;
                case 3:
                    stergeCarte();
                    break;
                case 4:
                    afiseazaCarti();
                    break;
                case 5:
                    cautaCarte();
                    break;
                case 6:
                    filtrareCarti();
                    break;
                case 7:
                    sortareCarti();
                    break;
                case 0:
                    return;
                default:
                    cout << "Comanda invalida\n";
            }
        }catch (RepoException& e){
            cout << e.getMsg() << "\n";
        }catch (ValidatorException& e){
            cout << e.getMsg() << "\n";
        }
    }
}

void Console::adaugaCarte() {
    string titlu, autor, gen;
    int anAparitie;
    cout << "Titlu: ";
    cin >> titlu;
    cout << "Autor: ";
    cin >> autor;
    cout << "Gen: ";
    cin >> gen;
    cout << "An aparitie: ";
    cin >> anAparitie;
    service.addService(titlu, autor, gen, anAparitie);
}

void Console::modificaCarte() {
    string titlu, autor, genNou;
    int anAparitieNou;
    cout << "Titlul cartii de modificat: ";
    cin >> titlu;
    cout << "Autorul cartii de modificat: ";
    cin >> autor;
    cout << "Noul gen: ";
    cin >> genNou;
    cout << "Noul an aparitie: ";
    cin >> anAparitieNou;
    service.modifyService(titlu, autor, genNou, anAparitieNou);
}

void Console::stergeCarte() {
    string titlu, autor;
    cout << "Titlul cartii de sters: ";
    cin >> titlu;
    cout << "Autorul cartii de sters: ";
    cin >> autor;
    service.deleteService(titlu, autor);
}

void Console::tiparesteLista(vector<Carte> &carti) {
    for (const auto& carte : carti){
        cout << carte.getTitlu() << "-" << carte.getAutor() << "; " << carte.getGen() << "; " << carte.getAnAparitie() << "\n";
    }
}

void Console::afiseazaCarti() {
    vector<Carte> carti = service.getAllService();
    tiparesteLista(carti);
}

void Console::cautaCarte() {
    string titlu, autor;
    cout << "Titlul cartii de cautat: ";
    cin >> titlu;
    cout << "Autorul cartii de cautat: ";
    cin >> autor;
    Carte& carte = service.searchService(titlu, autor);
    cout << carte.getTitlu() << "-" << carte.getAutor() << "; " << carte.getGen() << "; " << carte.getAnAparitie() << "\n";
}

void Console::filtrareCarti() {
    int cmd;
    cout << "1.Filtrare dupa titlu\n2.Filtrare dupa an aparitie\n";
    cin >> cmd;
    if (cmd == 1) {
        string titlu;
        cout << "Titlul dupa care se filtreaza: ";
        cin >> titlu;
        vector<Carte> carti = service.filterByTitlu(titlu);
        tiparesteLista(carti);
    } else if (cmd == 2) {
        int anAparitie;
        cout << "Anul dupa care se filtreaza: ";
        cin >> anAparitie;
        vector<Carte> carti = service.filterByAnAparitie(anAparitie);
        tiparesteLista(carti);
    } else {
        cout << "Comanda invalida\n";
    }
}

void Console::sortareCarti() {
    int cmd;
    cout << "1.Sortare dupa titlu\n2.Sortare dupa autor\n3.Sortare dupa an aparitie si gen\n";
    cin >> cmd;
    if (cmd == 1) {
        vector<Carte> carti = service.sortByTitlu();
        tiparesteLista(carti);
    } else if (cmd == 2) {
        vector<Carte> carti = service.sortByAutor();
        tiparesteLista(carti);
    } else if (cmd == 3) {
        vector<Carte> carti = service.sortByAnSiGen();
        tiparesteLista(carti);
    } else {
        cout << "Comanda invalida\n";
    }
}

void Console::adaugaCateva() {
    service.addService("Ion", "Liviu Rebreanu", "roman", 1930);
    service.addService("Enigma Otiliei", "George Calinescu", "roman", 1938);
    service.addService("Poezii", "Mihai Eminescu", "poezii", 1885);
    service.addService("TitluTest", "AutorTest", "roman", 1920);
    service.addService("Moara cu noroc", "Ioan Slavici", "nuvela", 1881);
    service.addService("Poezii", "George Bacovia", "poezii", 1920);
}