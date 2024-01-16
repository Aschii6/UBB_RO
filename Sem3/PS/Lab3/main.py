import random
from random import choices, sample, randrange
from math import comb, perm
from matplotlib.pyplot import bar, hist, grid, show, legend
from scipy.stats import binom


def ex1():
    print("EX1")
    n = 2000

    no1 = 0
    no2 = 0

    for i in range(n):
        red = 5
        blue = 3
        green = 2

        r = 0
        b = 0
        g = 0

        j = 0
        while j < 3:
            selected = random.randint(1, 3)

            if selected == 1:
                if red > 0:
                    red -= 1
                    r += 1
                else:
                    j -= 1
            elif selected == 2:
                if blue > 0:
                    blue -= 1
                    b += 1
                else:
                    j -= 1
            else:
                if green > 0:
                    green -= 1
                    g += 1
                else:
                    j -= 1
            j += 1

        A = r > 1
        B = r == 3 | b == 3 | g == 3

        if B & A:
            no1 += 1

        if A:
            no2 += 1

    if no1 > 0 and no2 > 0:
        print("Simultaed: P(B|A) = ", no1 / no2 * 100, "%")

    pA = 1 - (5/10 * 4/9 * 3/8)

    pAB = 5/10 * 4/9 * 3/8

    print("Calculated: P(B|A) = ", pAB / pA * 100, "%")


def ex2():
    data = [randrange(1, 7) for _ in range(500)]

    bin_edges = [k + 0.5 for k in range(0, 7)]

    hist(data, bin_edges, density=True, rwidth=0.9, color='green', edgecolor='black',
         alpha=0.5, label='Relative Frequencies')

    distribution = dict([(i, 1/6) for i in range(1, 7)])

    bar(list(distribution.keys()), list(distribution.values()), width=0.85, color='red', edgecolor='black',
        alpha=0.6, label='Probabilities')

    legend(loc='lower left')

    # show()


def ex3():
    print("EX3")
    balls1 = 6
    balls0 = 4

    n = 1000

    data = []

    for i in range(1000):
        sum = 0

        for j in range(5):
            c = random.randint(1, 10);
            if c <= 6:
                sum += 1
            else:
                sum += 0

        data.append(sum)

    print("List of values for X: ", data[:50])

    distribution = dict([(i, 0) for i in range(6)])

    for i in range(6):
        distribution[i] = binom.pmf(i, 5, 6/10)

    bin_edges = [k + 0.5 for k in range(-1, 6)]

    hist(data, bin_edges, density=True, rwidth=0.9, color='green', edgecolor='black',
         alpha=0.5, label='Relative Frequencies')

    bar(list(distribution.keys()), list(distribution.values()), width=0.85, color='red', edgecolor='black',
        alpha=0.6, label='Probabilities')

    legend(loc='upper left')

    # show()

    count = 0

    for i in data:
        if i > 2 and i <= 5:
            count += 1

    print("Estimated P(2 < X <= 5): ", count/10, "%")

    rez = binom.pmf(3, 5, 6/10) + binom.pmf(4, 5, 6/10) + binom.pmf(5, 5, 6/10)

    print("Calculated P(2 < X <= 5): ", rez * 100, "%")


def ex4():
    print("EX4")
    n = 2000

    maxNo = 0
    prefferedNo = 0

    data = [0 for i in range(19)]

    for i in range(1, 19):
        no = 0
        for j in range(n):
            rez = random.randint(1, 6) + random.randint(1, 6) + random.randint(1, 6)
            data[rez] += 1
            if i == rez:
                no += 1

        if no > maxNo:
            maxNo = no
            prefferedNo = i

    print("Best number by simulation: ", prefferedNo, ", with prob of appearing: ", maxNo / n * 100, "%")

    print("Best number calculated: 10 or 11, with prob of appearing: ", 100/8, "%")

    newData = data[3:]

    bin_edges = [k + 0.5 for k in range(2, 19)]

    # hist(newData, bin_edges, rwidth=0.9, color='green', edgecolor='black',
    #      alpha=0.5, label='Relative Frequencies')

    # ehh +-
    bar(range(3, 19), [(value / n) / 18 for value in newData], width=0.85, color='green', edgecolor='black', alpha=0.6,
        label='Relative Frequencies')

    distribution = dict([(3, 1/216), (4, 3/216), (5, 6/216), (6, 10/216), (7, 15/216),
                         (8, 21/216), (9, 25/216), (10, 27/217), (11, 27/217), (12, 25/216),
                         (13, 21/216), (14, 15/216), (15, 10/216), (16, 6/216), (17, 3/216), (18, 1/256)])

    bar(list(distribution.keys()), list(distribution.values()), width=0.85, color='red', edgecolor='black',
        alpha=0.6, label='Probabilities')

    legend(loc='upper left')

    # show()


if __name__ == '__main__':
    ex1()
    ex2()
    ex3()
    ex4()
