//
// Created by Daniel on 31-Mar-23.
//

#pragma once
#include <vector>
using std::vector;
#include "Carte.h"

class RepoException : public std::exception{
private:
    string msg;
public:
    /**
     * Constructor
     * @param msg string
     */
    explicit RepoException(string msg);

    /**
     * Getter pentru mesajul de eroare
     * @return string
     */
    string getMsg();
};

class Repo{
private:
    std::vector<Carte> carti;

public:
    /**
     * Constructor
     */
    Repo() = default;

    /**
     * Interzicem copierea unui obiect de tip Repo
     * @param ot
     */
    Repo(const Repo &ot) = delete;

    /**
     * Adauga o carte in repo
     * @param carte Carte
     * @throws RepoException daca exista deja o carte cu acelasi titlu si autor
     */
    void addCarte(const Carte& carte);

    /**
     * Returneaza toate cartile din repo
     * @return lista de carti
     */
    vector<Carte>& getAll();

    /**
     * Modifica o carte din repo
     * @param carte Carte
     * @throws RepoException daca nu exista o carte cu acelasi titlu si autor
     */
    void modifyCarte(const Carte& carte);

    /**
     * Sterge o carte din repo
     * @param carte Carte
     * @throws RepoException daca nu exista o carte cu acelasi titlu si autor
     */
    void deleteCarte(Carte& carte);

    /**
     * Cauta o carte in repo
     * @param titlu string
     * @param autor string
     * @return Carte
     * @throws RepoException daca nu exista o carte cu acelasi titlu si autor
     */
    Carte& searchCarte(string titlu, string autor);

    /**
     * Destructor
     */
    ~Repo() = default;
};
