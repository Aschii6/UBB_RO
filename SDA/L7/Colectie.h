#pragma once

#include <utility>
using std::pair;

typedef int TElem;

typedef pair<TElem, int> pereche;

class IteratorColectie;

class Colectie
{
	friend class IteratorColectie;

private:
	pereche* elemente;
    int* stanga;
    int* dreapta;
    int dimensiune;
    int capacitate;
    int primLiber;
    int radacina;

    void asiguraCapacitate();

    int cautaPrimLiber();

    [[nodiscard]] bool cautaRecursiv(TElem elem, int nod) const;

    [[nodiscard]] int nodSuccessor(TElem elem, int nod) const;

    [[noddiscard]] int functieNumar(int a, int b, int nod) const;
public:
		//constructorul implicit
		Colectie();

		//adauga un element in colectie
		void adauga(TElem e);

		//sterge o aparitie a unui element din colectie
		//returneaza adevarat daca s-a putut sterge
		bool sterge(TElem e);

		//verifica daca un element se afla in colectie
		[[nodiscard]] bool cauta(TElem elem) const;

		//returneaza numar de aparitii ale unui element in colectie
		[[nodiscard]] int nrAparitii(TElem elem) const;

		//intoarce numarul de elemente din colectie;
		[[nodiscard]] int dim() const;

		//verifica daca colectia e vida;
		[[nodiscard]] bool vida() const;

		//returneaza un iterator pe colectie
		[[nodiscard]] IteratorColectie iterator() const;

		// destructorul colectiei
		~Colectie();

};

