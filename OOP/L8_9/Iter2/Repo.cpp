//
// Created by User on 31/03/2023.
//

#include "Repo.h"
#include <utility>
#include <fstream>
#include <sstream>

using std::ifstream;
using std::ofstream;
using std::stoi;

RepoException::RepoException(std::string msg) {
    this->msg = std::move(msg);
}

string RepoException::getMsg() {
    return this->msg;
}

void Repo::addCarte(const Carte &carte) {
    if (path.length() > 0)
        readFromFile();
    for (auto& c : carti)
        if (c == carte)
            throw RepoException("Cartea exista deja!");
    carti.push_back(carte);
    if (path.length() > 0)
        writeToFile();
}

vector<Carte> &Repo::getAll() {
    if (path.length() > 0)
        readFromFile();
    return carti;
}

void Repo::modifyCarte(const Carte &carte) {
    if (path.length() > 0)
        readFromFile();
    auto it = std::find(carti.begin(), carti.end(), carte);
    if (it != carti.end())
        std::replace(carti.begin(), carti.end(), *it, carte);
    else
        throw RepoException("Cartea nu a fost gasita!");
    if (path.length() > 0)
        writeToFile();
}

void Repo::deleteCarte(Carte &carte) {
    if (path.length() > 0)
        readFromFile();
    auto it = std::find(carti.begin(), carti.end(), carte);
    if (it != carti.end())
        carti.erase(it);
    else
        throw RepoException("Cartea nu a fost gasita!");
    if (path.length() > 0)
        writeToFile();
}

Carte& Repo::searchCarte(string titlu, string autor) {
    if (path.length() > 0)
        readFromFile();
    Carte carte(std::move(titlu), std::move(autor), "", 1);
    auto it = std::find(carti.begin(), carti.end(), carte);
    if (it != carti.end())
        return *it;
    else
        throw RepoException("Cartea nu a fost gasita!");
}

void Repo::readFromFile() {
    ifstream f (path);

    string line;
    string token;

    carti.clear();
    while (getline(f, line)){
        vector<string> tmp;
        std::stringstream ss(line);
        while (getline(ss, token, ',')){
            tmp.push_back(token);
        }
        string titlu = tmp[0];
        string autor = tmp[1];
        string gen = tmp[2];
        int an = stoi(tmp[3]);
        Carte carte(titlu, autor, gen, an);
        carti.push_back(carte);
    }
    f.close();
}

void Repo::writeToFile() {
    ofstream g (path);
    for (const auto &carte: carti){
        g << carte.getTitlu() << ',' << carte.getAutor() << ',' << carte.getGen() << ',' << carte.getAnAparitie() << '\n';
    }
    g.close();
}