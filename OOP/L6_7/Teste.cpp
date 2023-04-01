//
// Created by Daniel on 31-Mar-23.
//

#include "Teste.h"
#include "Carte.h"
#include "Repo.h"
#include "Validator.h"
#include "Service.h"
#include <cassert>

void toateTestele(){
    testConstructor();
//    testSetteri();
    testRepo();
    testValidator();
    testService();
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

void testRepo(){
    testAddDelete();
    testModify();
    testSearch();
}

void testAddDelete(){
    Repo repo;
    Carte carte("Titlu", "Autor", "Gen", 2023);
    Carte carte2("Titlu2", "Autor2", "Gen2", 2024);

    assert(repo.getAll().empty());
    repo.addCarte(carte);
    assert(repo.getAll().size() == 1);
    repo.addCarte(carte2);
    assert(repo.getAll().size() == 2);

    try {
        repo.addCarte(carte);
        assert(false);
    } catch (RepoException& e){
        assert(e.getMsg() == "Cartea exista deja!");
    }

    repo.deleteCarte(carte2);
    assert(repo.getAll().size() == 1);
    repo.deleteCarte(carte);
    assert(repo.getAll().empty());

    try {
        repo.deleteCarte(carte);
        assert(false);
    } catch (RepoException& e){
        assert(e.getMsg() == "Cartea nu a fost gasita!");
    }
}

void testModify(){
    Repo repo;
    Carte carte("Titlu", "Autor", "Gen", 2023);

    repo.addCarte(carte);
    Carte carteDupaModificare("Titlu", "Autor", "altGen", 2022);
    repo.modifyCarte(carteDupaModificare);

    Carte carteDeVf = repo.getAll()[0];
    assert(carteDeVf.getGen() == "altGen");
    assert(carteDeVf.getAnAparitie() == 2022);

    Carte carte2("Titlu2", "Autor2", "Gen2", 2024);
    try {
        repo.modifyCarte(carte2);
        assert(false);
    } catch (RepoException& e){
        assert(e.getMsg() == "Cartea nu a fost gasita!");
    }
}

void testSearch(){
    Repo repo;
    Carte carte("Titlu", "Autor", "Gen", 2023);
    repo.addCarte(carte);

    Carte carteGasita = repo.searchCarte("Titlu", "Autor");
    assert(carteGasita.getTitlu() == "Titlu");
    assert(carteGasita.getAutor() == "Autor");
    assert(carteGasita.getAnAparitie() == 2023);

    try {
        repo.searchCarte("Titlu2", "Autor2");
        assert(false);
    } catch (RepoException& e){
        assert(e.getMsg() == "Cartea nu a fost gasita!");
    }
}

void testValidator(){
    Validator validator;
    Carte carte("Titlu", "Autor", "Gen", 2023);
    validator.validate(carte);

    Carte carte2("", "Autor", "Gen", 2023);
    try {
        validator.validate(carte2);
        assert(false);
    } catch (ValidatorException& e){
        assert(e.getMsg() == "Titlul nu poate fi vid! ");
    }

    Carte carte3("", "", "", -1);
    try {
        validator.validate(carte3);
        assert(false);
    } catch (ValidatorException& e){
        assert(e.getMsg() == "Titlul nu poate fi vid! Autorul nu poate fi vid! Genul nu poate fi vid! Anul aparitiei nu poate fi negativ!");
    }
}

void testService(){
    testAddDeleteService();
    testModifyService();
    testSearchService();
    testFilters();
    testSorts();
}

void testAddDeleteService() {
    Repo repo;
    Validator validator;
    Service service(repo, validator);

    assert(service.getAllService().empty());
    service.addService("Titlu", "Autor", "Gen", 2023);
    assert(service.getAllService().size() == 1);
    service.addService("Titlu2", "Autor2", "Gen2", 2024);
    assert(service.getAllService().size() == 2);

    try {
        service.addService("Titlu", "Autor", "Gen", 2023);
        assert(false);
    } catch (RepoException& e){
        assert(e.getMsg() == "Cartea exista deja!");
    }

    service.deleteService("Titlu2", "Autor2");
    assert(service.getAllService().size() == 1);
    service.deleteService("Titlu", "Autor");
    assert(service.getAllService().empty());

    try {
        service.deleteService("Titlu", "Autor");
        assert(false);
    } catch (RepoException& e){
        assert(e.getMsg() == "Cartea nu a fost gasita!");
    }
}

void testModifyService(){
    Repo repo;
    Validator validator;
    Service service(repo, validator);

    service.addService("Titlu", "Autor", "Gen", 2023);
    service.modifyService("Titlu", "Autor", "altGen", 2022);

    Carte carteDeVf = service.getAllService()[0];
    assert(carteDeVf.getGen() == "altGen");
    assert(carteDeVf.getAnAparitie() == 2022);

    try {
        service.modifyService("Titlu2", "Autor2", "altGen", 2022);
        assert(false);
    } catch (RepoException& e){
        assert(e.getMsg() == "Cartea nu a fost gasita!");
    }
}

void testSearchService(){
    Repo repo;
    Validator validator;
    Service service(repo, validator);

    service.addService("Titlu", "Autor", "Gen", 2023);

    Carte carteGasita = service.searchService("Titlu", "Autor");
    assert(carteGasita.getTitlu() == "Titlu");
    assert(carteGasita.getAutor() == "Autor");
    assert(carteGasita.getAnAparitie() == 2023);

    try {
        service.searchService("Titlu2", "Autor2");
        assert(false);
    } catch (RepoException& e){
        assert(e.getMsg() == "Cartea nu a fost gasita!");
    }
}

void testFilters(){
    Repo repo;
    Validator validator;
    Service service(repo, validator);

    service.addService("Titlu", "Autor", "Gen", 2023);
    service.addService("Titlu2", "Autor2", "Gen2", 2024);
    service.addService("Titlu3", "Autor3", "Gen3", 2025);

    vector<Carte> cartiFiltrate = service.filterByTitlu("Titlu");
    assert(cartiFiltrate.size() == 1);
    assert(cartiFiltrate[0].getTitlu() == "Titlu");

    cartiFiltrate = service.filterByAnAparitie(2024);
    assert(cartiFiltrate.size() == 1);
    assert(cartiFiltrate[0].getAnAparitie() == 2024);
}

void testSorts(){
    Repo repo;
    Validator validator;
    Service service(repo, validator);

    service.addService("Titlu", "Autor", "Gen", 2023);
    service.addService("Titlu3", "Autor3", "Gen3", 2024);
    service.addService("Titlu2", "Autor2", "Gen2", 2024);

    vector<Carte> cartiSortate = service.sortByTitlu();
    assert(cartiSortate[0].getTitlu() == "Titlu");
    assert(cartiSortate[1].getTitlu() == "Titlu2");
    assert(cartiSortate[2].getTitlu() == "Titlu3");

    cartiSortate = service.sortByAutor();
    assert(cartiSortate[0].getAutor() == "Autor");
    assert(cartiSortate[1].getAutor() == "Autor2");
    assert(cartiSortate[2].getAutor() == "Autor3");

    cartiSortate = service.sortByAnSiGen();
    assert(cartiSortate[0].getAnAparitie() == 2023);
    assert(cartiSortate[1].getAnAparitie() == 2024 && cartiSortate[1].getGen() == "Gen2");
    assert(cartiSortate[2].getAnAparitie() == 2024 && cartiSortate[2].getGen() == "Gen3");
}