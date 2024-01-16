; a) functie suma 2 vectori

; umpleCu0(a: list, l:integer)
; a - lista primita
; l - nr de 0 care trebuie adaugati la inceput
; returneaza lista modificata
(defun umpleCu0(a l)
  (if (> l 0)
    (cons 0 (umpleCu0 a (- l 1)))
    a
  )
)

; sum2Vec_aux(a: list, b: list)
; a - prima lista, b - a doua lista
; returneaza un vector care este suma elementelor din vectori
(defun sum2Vec_aux(a b)
  (cond
    ((null a) nil)
    ((or (not (numberp (car a))) (not (numberp (car b)))) (sum2Vec_aux (cdr a) (cdr b)))
    (t(cons (+ (car a) (car b)) (sum2Vec_aux (cdr a) (cdr b))))
  )
)

; sum2Vec(a: list, b: list)
; a - prima lista, b - a doua lista
; returneaza un vector care este suma elementelor din vectori
(defun sum2Vec(a b)
  (if (> (length a) (length b))
    (sum2Vec_aux a (umpleCu0 b (- (length a) (length b))))
    (sum2Vec_aux b (umpleCu0 a (- (length b) (length a))))
  )
)

(print (sum2Vec '(1 2 3 4) '(5 6 7 8 9)))
(print (sum2Vec '(1 2 3 4 c) '(5 6 a 7 8 9 d)))

; b) Definiti o functie care obtine dintr-o lista data lista tuturor atomilor 
; care apar, pe orice nivel, dar in aceeasi ordine.

; my_append(l: list, k: list)
; l - lista la care se adauga
; k - lista care se adauga la final
; returneaza rezultatul concatenarii lui k la sfarsitul lui l
(defun my_append (l k)
    (if (null l) 
        k
        (cons (car l) (my_append (cdr l) k))
    )
)

; totiAtomii(l: list)
; lista primita
; returneaza o lista cu toti atomii din l
(defun totiAtomii (l)
    (cond
        ((null l) nil)
        ((listp (car l)) (my_append (totiAtomii (car l)) (totiAtomii (cdr l))))
        (t (cons (car l) (totiAtomii (cdr l))))
    )
)

(print (totiAtomii '(((A B) C) (D E))))

; c) Sa se scrie o functie care plecand de la o lista data ca argument,
; inverseaza numai secventele continue de atomi.

; my_reverse(l: list)
; l - lista de inversat
; returneaza l inversata
(defun my_reverse (l)
    (if (null l)
        nil
        (my_append (my_reverse (cdr l)) (list (car l)))
    )        
)

; invSecv(l: list, c: list)
; l - lista primita
; c - variabila colectoare
; returneaza lista prelucrata in care inverseaza elem de pe acelasi nivel
(defun invSecv (l c)
    (cond
        ((null l) (my_reverse c))
        ((listp (car l)) (my_append (my_reverse c) (cons (invSecv (car l) nil) (invSecv (cdr l) nil))))
        (T (invSecv (cdr l) (my_append c (list (car l)))))
    )
)

; invSecvMain(l: list)
; returneaza lista prelucrata in care inverseaza elem de pe acelasi nivel
(defun invSecvMain (l)
  (invSecv l nil)
)

(print (invSecvMain '(a b c (d (e f) g h i))))

; d) Sa se construiasca o functie care intoarce maximul atomilor numerici
; dintr-o lista, de la nivelul superficial.

; my_max(a: orice, b: orice)
; a - 
; returneaza maximul numeric dintre 2 numere sau nil daca a si b nu sunt numere
(defun my_max (a b)
    (cond
        ((and (not (numberp a)) (not (numberp b))) nil)
        ((not (numberp a)) b)
        ((not (numberp b)) a)
        ((> a b) a)
        (T b)
    )
)

; maxLista(l: list)
; l - lista primita
; returneaza cel mai mare numar de la nivelul superficial din lista
(defun maxLista (l)
    (if (null l) 
        nil
        (my_max (car l) (maxLista (cdr l)))
    )
)

(print (maxLista '(A 1 (B (4 20)) (5 X) 3 C F)))
