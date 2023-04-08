//
// Created by User on 01/04/2023.
//

#pragma once
#include "Carte.h"

class ValidatorException : public std::exception {
private:
    string msg;
public:
    explicit ValidatorException(string message);
    string getMsg();
};

class Validator {
public:
    /**
     * Constructorul implicit
     */
    Validator() = default;

    /**
     * Valideaza o carte
     * @param carte
     * @throws ValidatorException daca cartea nu este valida
     */
    static void validate(const Carte& carte);
};
