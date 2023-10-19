//
// Created by Daniel on 03-Mar-23.
//

#include "Coada.h"
#include <exception>
#include <iostream>
using std::exception;

// complex teta(1)
Coada::Coada() {
    this->dimensiune = 1;
    this->lista = new TElem [this->dimensiune];
    this->front = 0;
    this->rear = -1;
}

// complex overall teta(1)
// la fiecare n elemente adaugate se efectueaza o operatie de n pasi
// T(n) = [(1 + 1 + 1 + ... + 1) de n - 1 ori + n + 1] / n = (2n)/n = 2 adica teta(1)
void Coada::adauga(TElem elem) {
    if (this->plina()){
        if(this->front == 0){
            auto *alta_lista = new TElem [this->dimensiune*2];
            for (int i=0; i<dimensiune; i++)
                alta_lista[i] = this->lista[i];
            delete[] this->lista;
            this->lista = alta_lista;
            this->dimensiune *= 2;
            this->rear++;
            this->lista[this->rear] = elem;
        } else{
            auto *alta_lista = new TElem [this->dimensiune*2];
            int j = 0;
            for (int i=this->front; i<this->dimensiune; i++){
                alta_lista[j] = this->lista[i];
                j++;
            }
            for (int i=0; i<this->front; i++){
                alta_lista[j] = this->lista[i];
                j++;
            }
            delete[] this->lista;
            this->lista = alta_lista;
            this->rear = this->dimensiune;
            this->dimensiune *= 2;
            this->front = 0;
            this->lista[this->rear] = elem;
        }
    } else{
        if (this->rear == this->dimensiune - 1)
            this->rear = 0;
        else
            this->rear++;
        this->lista[this->rear] = elem;
    }
}

// complexitate teta(1)
TElem Coada::element() const {
    if(this->vida())
        throw exception();
    return this->lista[this->front];
}

// complexitate teta(1)
TElem Coada::sterge() {
    if(this->vida())
        throw exception();
    TElem elem = this->lista[this->front];

    if (this->front == this->dimensiune - 1){
        if (this->rear == this->dimensiune - 1)
            this->rear = -1;
        this->front = 0;
    }
    else
        this->front++;

    return elem;
}

// complexitate teta(1)
bool Coada::vida() const {
    if (this->rear == this->front - 1)
        return true;
    return false;
}

// complexitate teta(1)
bool Coada::plina() const {
    if((this->front == 0 && this->rear == this->dimensiune - 1) || this->rear == this->front - 1)
        return true;
    return false;
}

// complexitate teta(n) ; n - nr de elemente din coada
void Coada::afiseaza_toate(){
    if (this->front <= this->rear){
        for (int i=this->front; i<=this->rear; i++)
            std::cout << this->lista[i] << " ";
    } else{
        for (int i=this->front; i<this->dimensiune; i++)
            std::cout << this->lista[i] << " ";
        for (int i=0; i<=this->rear; i++)
            std::cout << this->lista[i] << " ";
    }
    std::cout<<'\n';
}

// complexitate teta(1)
Coada::~Coada() {
    delete[] this->lista;
}
