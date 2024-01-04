import numpy as np
from scipy.stats import norm, uniform, expon
from numpy import mean, std, linspace, exp
from scipy.integrate import quad
from matplotlib.pyplot import show, hist, plot


def ex1():
    print("Ex 1")
    n = 2000
    m = 165
    sigma = 10
    data = norm.rvs(m, sigma, size=n)
    hist(data, bins=16, density=True, range=(130, 200), color='orange', rwidth=0.9, label='Frecvente Relative')

    xValues = linspace(130, 200, 1000)
    pdfValues = norm.pdf(xValues, m, sigma)

    plot(xValues, pdfValues)
    show()

    valMedieSim = mean(data)
    deviatieStandardSim = std(data)

    valoriInRaza = np.sum((data >= 160) & (data <= 170))

    proportieDeValoriSim = valoriInRaza / n

    print("Valori simulate: valoarea medie", valMedieSim, ",deviatie standard", deviatieStandardSim,
          ",proporite de valori", proportieDeValoriSim)

    valMedie = norm.mean(165, 10)
    deviatieStandard = norm.std(165, 10)
    proportieDeValori = (norm.cdf(170, valMedie, deviatieStandard) -
                         norm.cdf(160, valMedie, deviatieStandard))

    print("Valori exacte: valoarea medie", valMedie, ",deviatie standard", deviatieStandard,
          ",proportie de valori", proportieDeValori)


def ex2():
    print("\nEx 2")
    n = 2000
    P1 = expon.rvs(loc=0, scale=5, size=n)
    P2 = uniform.rvs(loc=4, scale=2, size=n)

    PF = np.concatenate([P1, P2])

    mean1 = mean(P1)
    mean2 = mean(P2)

    std1 = std(P1)
    std2 = std(P2)

    meanF = mean1 * 0.4 + mean2 * 0.6
    stdF = std1 * 0.4 + std2 * 0.6

    print("Valori estimate: val medie", meanF, ",deviatie standard", stdF)

    probE = np.sum(PF < 5) / (2 * n)

    print("Probabilitate estimata:", probE)

    probC = 0.4 * expon.cdf(5, loc=0, scale=5) + 0.6 * (5 - 4) * 1/2

    print("Probabilitate calculata:", probC)


def ex3():
    print("\nEx 3")
    b = 3
    a = -1
    n = 5000

    g = lambda x: exp(-(x ** 2))
    U = uniform.rvs(a, b-a, size=n)

    rez1 = quad(g, -1, 3)[0]

    sum = 0
    for u in U:
        sum += g(u)

    rez2 = sum / n * (b - a)

    print("Quad:", rez1, ".Aproximare cu suma:", rez2)


if __name__ == '__main__':
    ex1()
    ex2()
    ex3()
