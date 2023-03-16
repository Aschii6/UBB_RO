//
// Created by Daniel on 16-Mar-23.
//

#pragma once
#include "Masina.h"
#include "Lista.h"

typedef int (*functieCmp)(Masina* m1, Masina* m2);

void mySort(Lista* l, functieCmp fCmp);
