from random import sample
from matplotlib.pyplot import hist, show
from scipy.stats import bernoulli, geom, hypergeom


def ex1(p=0.5, pasi=200, n=1000, toShow=True):
    # a
    nod = 0
    pozitiiNod = []

    for i in range(pasi):
        pozitiiNod.append(nod)
        nod += (bernoulli.rvs(p) * 2 - 1)

    print("a) Poz: ", pozitiiNod)

    # b
    data = []

    for i in range(n):
        nod = 0
        for j in range(pasi):
            nod += (bernoulli.rvs(p) * 2 - 1)

        data.append(nod)

    if toShow:
        left = min(data)
        right = max(data)

        bin_edges = [k + 0.5 for k in range(left - 1, right + 1)]
        hist(data, bin_edges, density=True, rwidth=0.9, color='red', edgecolor='black')
        show()


def ex1c(p=0.5, pasi=200, toShow=True):
    start = 0

    nCerc = 50

    n = 1000
    data = []

    for i in range(n):
        nod = start
        for j in range(pasi):
            nod += (bernoulli.rvs(p) * 2 - 1)

            if nod < 0:
                nod = nCerc - 1
            if nod == nCerc:
                nod = 0

        data.append(nod)

    if toShow:
        left = min(data)
        right = max(data)

        bin_edges = [k + 0.5 for k in range(left - 1, right + 1)]
        hist(data, bin_edges, density=True, rwidth=0.9, color='red', edgecolor='black')
        show()


def ex2(pasi=500):
    lista = []
    for i in range(pasi):
        total = 0
        castigat = False

        while not castigat:
            bileAlese = sample(range(1, 50), 6)

            bileCastigatoare = sample(range(1, 50), 6)

            corecte = 0

            for bila in bileAlese:
                if bila in bileCastigatoare:
                    corecte += 1

            # corecte = hypergeom.rvs(49, 6, 6)

            if corecte >= 3:
                castigat = True

            total += 1

        lista.append(total - 1)

    print("Bilete cumparate pana la 'castig': ", lista)

    areLoc = 0
    for nr in lista:
        if nr >= 10:
            areLoc += 1

    print("Probabilitate simulata pentru evenimentul dat este: ", areLoc / pasi * 100, '%')

    p = sum(hypergeom.pmf(k, 49, 6, 6) for k in range(3, 7))

    probTeoretica = 1 - geom.cdf(9, p)

    print("Prob teoretica ptr eveniment este: ", probTeoretica * 100, '%')


if __name__ == '__main__':
    ex1(toShow=False)
    ex1c(toShow=False)
    ex2()
