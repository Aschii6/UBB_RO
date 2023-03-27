//
// Created by Daniel on 16-Mar-23.
//

#include "FunctieSort.h"

void sort(Lista* l, functieCmp fCmp){
    for (int i = 0; i < dimensiune(l); i++) {
        for (int j = i + 1; j < dimensiune(l); j++) {
            Masina m1 = elem(l, i);
            Masina m2 = elem(l, j);
            if (fCmp(&m1, &m2) > 0){
                modifica(l, i, m2);
                modifica(l, j, m1);
            }
        }
        
    }
}