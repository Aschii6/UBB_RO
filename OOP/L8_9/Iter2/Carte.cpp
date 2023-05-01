//
// Created by User on 31/03/2023.
//

#include "Carte.h"

Carte::Carte(string titlu, string autor, string gen, int anAparitie){
    this->titlu = std::move(titlu);
    this->autor = std::move(autor);
    this->gen = std::move(gen);
    this->anAparitie = anAparitie;
}

int Carte::operator==(const Carte& carte){
    return this->titlu == carte.titlu && this->autor == carte.autor;
}