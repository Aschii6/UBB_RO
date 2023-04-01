//
// Created by User on 31/03/2023.
//

#include "Service.h"
using std::string;

Service::Service(Repo &repo, Validator &validator) : repo(repo), validator(validator) {}

void Service::addService(const string &titlu, const string &autor, const string &gen, int anAparitie) {
    Carte carte(titlu, autor, gen, anAparitie);
    validator.validate(carte);
    repo.addCarte(carte);
}

const vector<Carte>& Service::getAllService() {
    return repo.getAll();
}

void Service::modifyService(const string &titlu, const string &autor, const string &gen, int anAparitie) {
    Carte carte(titlu, autor, gen, anAparitie);
    validator.validate(carte);
    repo.modifyCarte(carte);
}

void Service::deleteService(const string &titlu, const string &autor) {
    Carte carte(titlu, autor, "temp", 1);
    validator.validate(carte);
    repo.deleteCarte(carte);
}

Carte& Service::searchService(const string &titlu, const string &autor) {
    return repo.searchCarte(titlu, autor);
}

vector<Carte> Service::filter(const function<bool(const Carte &)> &fct) {
    vector<Carte> rez;
    for (auto& c : repo.getAll())
        if (fct(c))
            rez.push_back(c);
    return rez;
}

vector<Carte> Service::filterByTitlu(const string &titlu) {
    return filter([titlu](const Carte& c){ return c.getTitlu() == titlu; });
}

vector<Carte> Service::filterByAnAparitie(int anAparitie) {
    return filter([anAparitie](const Carte& c){ return c.getAnAparitie() == anAparitie; });
}

vector<Carte> Service::sort(const function<bool(const Carte &, const Carte &)> &fct) {
    vector<Carte> rez = repo.getAll();
    for (int i = 0; i < rez.size(); i++)
        for (int j = i + 1; j < rez.size(); j++)
            if (fct(rez[i], rez[j]))
                std::swap(rez[i], rez[j]);
    return rez;
}

vector<Carte> Service::sortByTitlu() {
    return sort([](const Carte& c1, const Carte& c2){ return c1.getTitlu() > c2.getTitlu(); });
}

vector<Carte> Service::sortByAutor() {
    return sort([](const Carte& c1, const Carte& c2){ return c1.getAutor() > c2.getAutor(); });
}

vector<Carte> Service::sortByAnSiGen() {
    return sort([](const Carte& c1, const Carte& c2){
        if (c1.getAnAparitie() == c2.getAnAparitie())
            return c1.getGen() > c2.getGen();
        return c1.getAnAparitie() > c2.getAnAparitie();
    });
}