//
// Created by User on 01/04/2023.
//

#include "Validator.h"

ValidatorException::ValidatorException(string message){
    this->msg = std::move(message);
}

string ValidatorException::getMsg(){
    return this->msg;
}

void Validator::validate(const Carte &carte) {
    string erori;
    if (carte.getTitlu().length() == 0)
        erori += "Titlul nu poate fi vid! ";
    if (carte.getAutor().length() == 0)
        erori += "Autorul nu poate fi vid! ";
    if (carte.getGen().length() == 0)
        erori += "Genul nu poate fi vid! ";
    if (carte.getAnAparitie() < 0)
        erori += "Anul aparitiei nu poate fi negativ!";

    if (erori.length() > 0)
        throw ValidatorException(erori);
}