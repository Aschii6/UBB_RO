//
// Created by Daniel on 31-Mar-23.
//

#pragma once
#include <string>
#include <utility>
using std::string;

class Carte{
private:
    string titlu;
    string autor;
    string gen;
    int anAparitie;

public:
    /**
     * Constructorul clasei Carte
     * @param titlu string
     * @param autor string
     * @param gen string
     * @param anAparitie int
     */
    Carte(string titlu, string autor, string gen, int anAparitie);

    /**
     * Constructorul de copiere al clasei Carte
     * @param ot - Carte
     */
    Carte(const Carte &ot) = default;

    /**
     * Getter pentru titlu
     * @return string
     */
    [[nodiscard]] string getTitlu() const{
        return titlu;
    }

    /**
     * Getter pentru autor
     * @return string
     */
    [[nodiscard]] string getAutor() const{
        return autor;
    }

    /**
     * Getter pentru gen
     * @return string
     */
    [[nodiscard]] string getGen() const{
        return gen;
    }

    /**
     * Getter pentru anul aparitiei
     * @return int
     */
    [[nodiscard]] int getAnAparitie() const{
        return anAparitie;
    }

    /**
     * Aduaga operatorul de egalitate intre obiecte de tip Carte
     * @param carte - Carte
     * @return int
     */
    int operator==(const Carte& carte);

    /**
     * Destructorul clasei Carte
     */
    ~Carte() = default;
};
