//
// Created by User on 31/03/2023.
//

#include "Service.h"
using std::string;

Service::Service(Repo &repo, Validator &validator, CosInchirieri &cos) : repo(repo), validator(validator), cos(cos) {}

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
    std::copy_if(repo.getAll().begin(), repo.getAll().end(), std::back_inserter(rez), fct);
    return rez;}

vector<Carte> Service::filterByTitlu(const string &titlu) {
    return filter([titlu](const Carte& c){ return c.getTitlu() == titlu; });
}

vector<Carte> Service::filterByAnAparitie(int anAparitie) {
    return filter([anAparitie](const Carte& c){ return c.getAnAparitie() == anAparitie; });
}

vector<Carte> Service::sort(const function<bool(const Carte &, const Carte &)> &fct) {
    vector<Carte> rez = repo.getAll();
    std::sort(rez.begin(), rez.end(), fct);
    return rez;}

vector<Carte> Service::sortByTitlu() {
    return sort([](const Carte& c1, const Carte& c2){ return c1.getTitlu() < c2.getTitlu(); });
}

vector<Carte> Service::sortByAutor() {
    return sort([](const Carte& c1, const Carte& c2){ return c1.getAutor() < c2.getAutor(); });
}

vector<Carte> Service::sortByAnSiGen() {
    return sort([](const Carte& c1, const Carte& c2){
        if (c1.getAnAparitie() == c2.getAnAparitie())
            return c1.getGen() < c2.getGen();
        return c1.getAnAparitie() < c2.getAnAparitie();
    });
}

int Service::sizeCos() {
    return cos.dimensiune();
}

void Service::addCos(Carte &c) {
    cos.add(c);
}

void Service::emptyCos() {
    cos.clear();
}

void Service::fillCos(int nr) {
    cos.addRandom(nr, repo.getAll());
}

vector<Carte> Service::getAllCos() {
    return cos.getAll();
}

void Service::exportCSV(string file) {
    vector<Carte> carti = cos.getAll();
    std::ofstream fout(file);

    for (auto &carte : carti)
        fout << carte.getTitlu() << ',' << carte.getAutor() << ',' << carte.getGen() << ',' << carte.getAnAparitie() << '\n';

    fout.close();
}