; Alte probleme de la L3
; 1. Sa se construiasca o functie care intoarce adancimea unei liste

(defun adancime (l)
  (cond
    ((atom l) 0)
    (t(+ 1 (apply #'max (mapcar #'adancime l))))
  )
)

; (print (adancime '(1 2 (3 4 (5) (6)))))

; 2. Definiti o functie care obtine dintr-o lista data lista tuturor atomilor
; care apar, pe orice nivel, dar in aceeasi ordine. De exemplu
; (((A B) C) (D E)) --> (A B C D E)

(defun totiAtomii (l)
  (cond
    ((atom l) (list l))
    (t (apply #'append (mapcar #'totiAtomii l)))
  )
)

; (print (totiAtomii '(((A B) C) (D E (F)))))
  
; 3. Sa se construiasca o functie care verifica daca un atom e membru al unei liste nu neaparat liniara.

(defun sau (l)
  (cond
    ((null l) nil)
    ((eq (car l) t) t)
    (t (sau (cdr l)))
  )
)

(defun membru (l e)
  (cond
    ((and (atom l) (eq l e)) t)
    ((atom l) nil)
    (t (sau (mapcar #'(lambda (x) (membru x e)) l)))
  )
)

; (print (membru '(1 2 (3 (4) (5))) 4))

; 4. Sa se construiasca o functie care intoarce suma atomilor numerici
; dintr-o lista, de la orice nivel.

(defun suma (l)
  (cond
    ((numberp l) l)
    ((atom l) 0)
    (t (apply #'+ (mapcar #'suma l)))
  )
)

; (print (suma '(1 2 (3 (4) 5 (1 (2))))))

; 5. Definiti o functie care testeaza apartenenta unui nod intr-un arbore n-ar
; reprezentat sub forma (radacina lista_noduri_subarb1...lista_noduri_subarbn)
; Ex: arborelele este (a (b (c)) (d) (e (f))) si nodul este 'b => adevarat

; (print (membru '(a (b (c)) (d) (e (f))) 'b))

; 6. Sa se construiasca o functie care intoarce produsul atomilor numerici
; dintr-o lista, de la orice nivel.

(defun produs (l)
  (cond
    ;((null l) 1)
    ((numberp l) l)
    ((atom l) 1)
    (t (apply #'* (mapcar #'produs l)))
  )
)

; (print (produs '(1 2 (a b (3 (2) c) 2))))

; 7. Sa se scrie o functie care calculeaza suma numerelor pare minus suma
; numerelor impare la toate nivelurile unei liste.

(defun suma (l)
  (cond
    ((and (numberp l) (evenp l)) l)
    ((numberp l) (* l -1))
    ((atom l) 0)
    (t (apply #'+ (mapcar #'suma l)))
  )
)

; (print (suma '(2 3 a b (4 (6)) (8 7 (3)))))

; 8. Sa se construiasca o functie care intoarce maximul atomilor numerici
; dintr-o lista, de la orice nivel.

(defun maxLista (l)
  (cond
    ((numberp l) l)
    ((atom l) most-negative-fixnum)
    (t (apply #'max (mapcar #'maxLista l)))
  )
)

; (print (maxLista '(2 a b (3 (4) (1 (6))))))

; 9. Definiti o functie care substituie un element E prin elementele
; unei liste L1 la toate nivelurile unei liste date L.

(defun substCuLista (l elem l1)
  (cond
    ((eql l elem) l1)
    ((atom l) (list l))
    (t (list (apply #'append (mapcar #'(lambda (x) (substCuLista x elem l1)) l))))
  )
)

; wrapper
; (print (substCuLista '(1 2 (1 3 (1))) 1 '(a b)))

; 10. Definiti o functie care determina numarul nodurilor de pe nivelul k
; dintr-un arbore n-ar reprezentat sub forma (radacina lista_noduri_subarb1
; ... lista_noduri_subarbn) Ex: arborelele este (a (b (c)) (d) (e (f))) si
; k=1 => 3 noduri

(defun nrNoduri (l niv k)
  (cond
    ((and (atom l) (= niv k)) 1)
    ((atom l) 0)
    (t (apply #'+ (mapcar #'(lambda (x) (nrNoduri x (+ 1 niv) k)) l)))
  )
)

(defun nrNoduriMain (l k)
  (nrNoduri l -1 k)
)

; (print (nrNoduriMain '(a (b (c)) (d) (e (f))) 1))

; 11. Sa se scrie o functie care sterge toate aparitiile unui atom de la
; toate nivelurile unei liste.

(defun sterge (l atom)
  (cond
    ((eq l atom) nil)
    ((atom l) (list l))
    (t (list (apply #'append (mapcar #'(lambda (x) (sterge x atom)) l))))
  )
)

; wrapper
; (print (sterge '(1 2 (1 3 4 (1 (1 2)) 6 7) 1) '1))

; 12. Definiti o functie care inlocuieste un nod cu altul intr-un arbore n-ar
; reprezentat sub forma (radacina lista_noduri_subarb1...lista_noduri_subarbn)
; Ex: arborelele este (a (b (c)) (d) (e (f))) si nodul 'b se inlocuieste cu
; nodul 'g => arborele (a (g (c)) (d) (e (f)))

(defun substituie (l n1 n2)
  (cond
    ((eq l n1) n2)
    ((atom l) l)
    (t (mapcar #'(lambda (x) (substituie x n1 n2)) l))
  )
)

; (print (substituie '(a (b (c)) (d) (e (f))) 'b 'g))

; 13. Definiti o functie care substituie un element prin altul la toate
; nivelurile unei liste date.

; aceeasi functie ca sus

; 14. Definiti o functie care da adancimea unui arbore n-ar reprezentat sub forma
; (radacina lista_noduri_subarb1...lista_noduri_subarbn)
; Ex: adancimea arborelui este (a (b (c)) (d) (e (f))) este 3

; (print (adancime '(a (b (c)) (d) (e (f)))))

; 15. Sa se construiasca o functie care intoarce numarul atomilor dintr-o
; lista, de la orice nivel.

(defun nrAtomi (l)
  (cond
    ((atom l) 1)
    (t (apply #'+ (mapcar #'nrAtomi l)))
  )
)

; (print (nrAtomi '(a (b (c)) (d) (e (f))) ))

; 16. Definiti o functie care inverseaza o lista impreuna cu toate sublistele
; sale de pe orice nivel.

(defun my_reverse (l)
  (cond
    ((null l) nil)
    (t (append (my_reverse (cdr l)) (list (car l))))
  )
)

(defun inverseaza (l)
  (cond
    ((atom l) l)
    (t (my_reverse (mapcar #'inverseaza l)))
  )
)

(print (inverseaza '(1 2 (1 3 4 (1 (1 2)) 6 7) 1)))
