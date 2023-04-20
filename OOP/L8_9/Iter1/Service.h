//
// Created by Daniel on 31-Mar-23.
//

#pragma once
#include "Carte.h"
#include "Repo.h"
#include "Validator.h"
#include "CosInchirieri.h"
#include <vector>
#include <algorithm>
#include <functional>
#include <fstream>
using std::function;

class Service {
private:
    Repo &repo;
    Validator &validator;
    CosInchirieri &cos;

    /**
     * Functie generica de filtrare
     * @param fct functie de filtrare
     * @return lista de carti
     */
    vector<Carte> filter(const function<bool(const Carte&)> &fct);

    /**
     * Functie generica de sortare
     * @param fct functie de comparare
     * @return lista de carti
     */
    vector<Carte> sort(const function<bool(const Carte&, const Carte&)> &fct);
public:
    /**
     * Constructor
     * @param repo
     * @param validator
     * @param cos
     */
    Service(Repo &repo, Validator &validator, CosInchirieri &cos);

    /**
     * Interzicem copierea
     * @param ot
     */
    Service(Service &ot) = delete;

    /**
     * Adauga o carte in service
     * @param titlu string
     * @param autor string
     * @param gen string
     * @param anAparitie int
     */
    void addService(const string &titlu, const string &autor, const string &gen, int anAparitie);

    /**
     * Returneaza toate cartile din service
     * @return lista de carti
     */
    const vector<Carte>& getAllService();

    /**
     * Modifica o carte din service
     * @param titlu string
     * @param autor string
     * @param gen string
     * @param anAparitie int
     */
    void modifyService(const string &titlu, const string &autor, const string &gen, int anAparitie);

    /**
     * Sterge o carte din service
     * @param titlu string
     * @param autor string
     */
    void deleteService(const string &titlu, const string &autor);

    /**
     * Cauta o carte in service
     * @param titlu string
     * @param autor string
     * @return Carte&
     */
    Carte& searchService(const string &titlu, const string &autor);

    /**
     * Filtrare dupa titlu
     * @param titlu
     * @return lista de carti
     */
    vector<Carte> filterByTitlu(const string &titlu);

    /**
     * Filtrare dupa anul aparitiei
     * @param anAparitie
     * @return lista de carti
     */
    vector<Carte> filterByAnAparitie(int anAparitie);

    /**
     * Sortare dupa titlu
     * @return lista de carti
     */
    vector<Carte> sortByTitlu();

    /**
     * Sortare dupa autor
     * @return lista de carti
     */
    vector<Carte> sortByAutor();

    /**
     * Sortare dupa an aparitie si gen
     * @return lista de carti
     */
    vector<Carte> sortByAnSiGen();

    int sizeCos();

    void addCos(Carte &c);

    void emptyCos();

    void fillCos(int nr);

    vector<Carte> getAllCos();

    void exportCSV(string file);

    /**
     * Destructor
     */
    ~Service() = default;
};