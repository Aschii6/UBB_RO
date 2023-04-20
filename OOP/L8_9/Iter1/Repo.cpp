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
    auto it = std::find(carti.begin(), carti.end(), carte);
    if (it != carti.end())
        std::replace(carti.begin(), carti.end(), *it, carte);
    else
        throw RepoException("Cartea nu a fost gasita!");
}

void Repo::deleteCarte(Carte &carte) {
    auto it = std::find(carti.begin(), carti.end(), carte);
    if (it != carti.end())
        carti.erase(it);
    else
        throw RepoException("Cartea nu a fost gasita!");
}

Carte &Repo::searchCarte(string titlu, string autor) {
    Carte carte(std::move(titlu), std::move(autor), "", 1);
    auto it = std::find(carti.begin(), carti.end(), carte);
    if (it != carti.end())
        return *it;
    else
        throw RepoException("Cartea nu a fost gasita!");
}