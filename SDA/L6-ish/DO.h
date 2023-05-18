#pragma once

typedef int TCheie;
typedef int TValoare;

#define NULL_TVALOARE (-1)

#include <utility>
typedef std::pair<TCheie, TValoare> TElem;

class Iterator;

typedef bool(*Relatie)(TCheie, TCheie);

class DO {
	friend class Iterator;
private:
	int dimensiune, capacitate;
    TElem* elems;
    int* urm;
    Relatie rel;
    int primLiber;

    [[nodiscard]] int hashFunction(TCheie c) const;
    void asiguraDimensiune();
    [[nodiscard]] bool factorAtins() const;

public:
	// constructorul implicit al dictionarului
	explicit DO(Relatie r);


	// adauga o pereche (cheie, valoare) in dictionar
	//daca exista deja cheia in dictionar, inlocuieste valoarea asociata cheii si returneaza vechea valoare
	// daca nu exista cheia, adauga perechea si returneaza null: NULL_TVALOARE
	TValoare adauga(TCheie c, TValoare v);

	//cauta o cheie si returneaza valoarea asociata (daca dictionarul contine cheia) sau null: NULL_TVALOARE
	[[nodiscard]] TValoare cauta(TCheie c) const;


	//sterge o cheie si returneaza valoarea asociata (daca exista) sau null: NULL_TVALOARE
	TValoare sterge(TCheie c);

	//returneaza numarul de perechi (cheie, valoare) din dictionar
	[[nodiscard]] int dim() const;

	//verifica daca dictionarul e vid
	[[nodiscard]] bool vid() const;

	// se returneaza iterator pe dictionar
	// iteratorul va returna perechile in ordine dupa relatia de ordine (pe cheie)
	[[nodiscard]] Iterator iterator() const;


	// destructorul dictionarului
	~DO();
};
