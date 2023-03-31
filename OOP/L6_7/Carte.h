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
    Carte(string titlu, string autor, string gen, int anAparitie){
        this->titlu = std::move(titlu);
        this->autor = std::move(autor);
        this->gen = std::move(gen);
        this->anAparitie = anAparitie;
    }

    Carte(const Carte& other){
        this->titlu = other.titlu;
        this->autor = other.autor;
        this->gen = other.gen;
        this->anAparitie = other.anAparitie;
    }

    [[nodiscard]] string getTitlu() const{
        return titlu;
    }

    [[nodiscard]] string getAutor() const{
        return autor;
    }

    [[nodiscard]] string getGen() const{
        return gen;
    }

    [[nodiscard]] int getAnAparitie() const{
        return anAparitie;
    }

    void setTitlu(string titlu){
        this->titlu = std::move(titlu);
    }

    void setAutor(string autor){
        this->autor = std::move(autor);
    }

    void setGen(string gen){
        this->gen = std::move(gen);
    }

    void setAnAparitie(int anAparitie){
        this->anAparitie = anAparitie;
    }

    int operator==(const Carte& carte){
        return this->titlu == carte.titlu && this->autor == carte.autor;
    }

    ~Carte() = default;
};