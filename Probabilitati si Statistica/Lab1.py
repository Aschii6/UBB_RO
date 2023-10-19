from random import sample
from math import factorial, perm, comb
from itertools import permutations, combinations


def aranjamente(string, nr, numarTotal = False, aleator=False):
    if numarTotal is True:
        print(perm(len(string), nr))
        return

    aranj = permutations(string, nr)

    lista = []
    for a in aranj:
        lista.append(a)

    if aleator is True:
        print(sample(lista, 1))
        return

    print(lista)

def combinari(string, nr, numarTotal = False, aleator=False):
    if numarTotal is True:
        print(comb(len(word), nr))
        return

    com = combinations(string, nr)

    lista = []
    for b in com:
        lista.append(b)

    if aleator is True:
        print(sample(lista, 1))
        return

    print(lista)

if __name__ == '__main__':
    word = "word"

    # 2
    for a in permutations(word):
        print(a)

    print(factorial(len(word)))

    lista = []
    for a in permutations(word):
        lista.append(a)

    print(sample(lista, 1))
    print()

    # 3
    aranjamente(word, 2, numarTotal=True)

    aranjamente(word, 2, aleator=True)

    combinari(word, 2)

    combinari(word, 2, numarTotal=True)

    combinari(word, 2, aleator=True)