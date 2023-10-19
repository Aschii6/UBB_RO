from random import randint, random
import matplotlib.pyplot as plt
from math import dist

def getListWith23Birthdays():
    l = []
    i = 0
    while i < 23:
        l.append(randint(1, 365))
        i += 1

    return l

def birthdayProbability(n):
    probability = 1.0

    for i in range(n):
        probability *= (365 - i) / 365

    return 1 - probability

if __name__ == '__main__':
    # 1.a
    nrOfTrials = randint(1000, 3000)
    nrOfGoalsReached = 0

    for i in range (nrOfTrials):
        l = getListWith23Birthdays();
        for i in range (len(l)):
            for j in range (i + 1, len(l)):
                if l[i] == l[j]:
                    nrOfGoalsReached += 1

    print("Chance for simulated results: ", nrOfGoalsReached / nrOfTrials * 100, '%')

    # 1.b
    print("For calculated probability: ", birthdayProbability(23) * 100, '%')

    # 2
    n = 1000

    nrA = 0
    nrB = 0
    nrC = 0

    square = plt.axis((0, 1, 0, 1))

    E = [0.5, 0.5]

    for i in range(n):
        tmp = [random(), random()]
        # a
        plt.plot(E[0], E[1], "ro")
        if dist(E, tmp) < 0.5:
            nrA += 1
            plt.plot(tmp[0], tmp[1], "bo")

        # b
        ok = 1
        for j in [[0, 0], [0, 1], [1, 0], [1, 1]]:
            if (dist(j, tmp) < dist(E, tmp)):
                ok = 0

        if ok == 1:
            nrB += 1
            # plt.plot(tmp[0], tmp[1], "yo")

        # c
        # sa fie in acelasi semicercuri de la 2 laturi / distanta fata de 2 mijloce sa fie mai mica de 0.5
        aux = 0
        for j in [[0, 0.5], [0.5, 0], [0.5, 1], [1, 0.5]]:
            if (dist(j, tmp) < 0.5):
                aux += 1

        if aux >= 2: # == 2?
            nrC += 1
            # plt.plot(tmp[0], tmp[1], "mo")


    print("Percentage of points in interior of the circle tangent to the square: ", nrA / n * 100, '%')

    print("Percentage of points closer to centre of circle than edges of square: ", nrB / n * 100, '%')

    print("Percentage of points that form with the edges of the square 2 acute triangles and 2 obtuse trinagles : ", nrC / n * 100, '%')

    # for C

    print("Geometric probability for points in interior of the circle tangent to the square: ", 0.5 * 0.5 * 3.14 * 100, '%')
    print("Geometric probability for points closer to centre of circle than edges of square: ", 0.5 * 1.41 * 0.5 * 1.41 * 100, '%')

    plt.show()
