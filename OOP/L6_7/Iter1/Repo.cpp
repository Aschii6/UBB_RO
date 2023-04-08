//
// Created by User on 31/03/2023.
//

#include "Repo.h"

#include <utility>

RepoException::RepoException(std::string msg) {
    this->msg = std::move(msg);
}

string RepoException::getMsg() {
    return this->msg;
}

void Repo::addCarte(const Carte &carte) {
    for (auto& c : carti)
        if (c == carte)
            throw RepoException("Cartea exista deja!");
    carti.push_back(carte);
}

vector<Carte> &Repo::getAll() {
    return carti;
}

void Repo::modifyCarte(const Carte &carte) {
    int poz = 0;
    for (auto& c : carti){
        if (c == carte){
            carti[poz] = carte;
            return;
        }
        poz ++;
    }
    throw RepoException("Cartea nu a fost gasita!");
}

void Repo::deleteCarte(Carte &carte) {
    int poz = 0;
    for (auto& c : carti){
        if (c == carte){
            carti.erase(carti.begin()+poz);
            return;
        }
        poz ++;
    }
    throw RepoException("Cartea nu a fost gasita!");
}

Carte &Repo::searchCarte(string titlu, string autor) {
    Carte carte(std::move(titlu), std::move(autor), "", 1);
    for (auto& c : carti)
        if (c == carte)
            return c;
    throw RepoException("Cartea nu a fost gasita!");
}
