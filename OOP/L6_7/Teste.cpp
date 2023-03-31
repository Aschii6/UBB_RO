//
// Created by Daniel on 31-Mar-23.
//

#include "Teste.h"
#include "Carte.h"
#include <cassert>

void toateTestele(){
    testConstructor();
    testSetteri();
}

void testConstructor(){
    Carte carte("Titlu", "Autor", "Gen", 2023);
    assert(carte.getTitlu() == "Titlu");
    assert(carte.getAutor() == "Autor");
    assert(carte.getGen() == "Gen");
    assert(carte.getAnAparitie() == 2023);

    Carte carte2("Titlu", "Autor", "Gen", 2023);
    assert(carte == carte2);
}

void testSetteri(){
    Carte carte("Titlu", "Autor", "Gen", 2023);
    carte.setTitlu("Titlu2");
    carte.setAutor("Autor2");
    carte.setGen("Gen2");
    carte.setAnAparitie(2024);
    assert(carte.getTitlu() == "Titlu2");
    assert(carte.getAutor() == "Autor2");
    assert(carte.getGen() == "Gen2");
    assert(carte.getAnAparitie() == 2024);
}