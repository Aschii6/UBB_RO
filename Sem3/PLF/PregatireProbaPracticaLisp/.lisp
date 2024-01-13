; L1
; 1 a) Sa se insereze intr-o lista liniara un atom a dat dupa al 2-lea, al 4-lea,
; al 6-lea,....element.
; b) Definiti o functie care obtine dintr-o lista data lista tuturor atomilor
; care apar, pe orice nivel, dar in ordine inversa. De exemplu: (((A B) C)
; (D E)) --> (E D C B A)
; c) Definiti o functie care intoarce cel mai mare divizor comun al numerelor
; dintr-o lista neliniara.
; d) Sa se scrie o functie care determina numarul de aparitii ale unui atom dat
; intr-o lista neliniara.

(defun insertPar_aux (l a c)
  (cond
    ((null l) nil)
    ((evenp c) (cons (car l) (cons a (insertPar_aux (cdr l) a (+ c 1)))))
    (t (cons (car l) (insertPar_aux (cdr l) a (+ c 1))))
  )
)

(defun insertPar (l a)
  (insertPar_aux l a 1)
)

;(print (insertPar '(1 2 3 4 5) 'a))

(defun ordineInversaOriceNivel (l)
  (cond
    ((null l) nil)
    ((listp (car l)) (append (ordineInversaOriceNivel (cdr l)) (ordineInversaOriceNivel (car l))))
    (t (append (ordineInversaOriceNivel (cdr l)) (list (car l))))
  )
)

; (print (ordineInversaOriceNivel '(a (b c) (d e (f g) h) i j)))

(defun cmmdc (a b)
  (cond
    ((not (numberp a)) b)
    ((not (numberp b)) a)
    ((= a b) a)
    ((> a b) (cmmdc (- a b) b))
    (t(cmmdc a (- b a)))
  )
)

(defun cmmdcLista (l)
  (cond
    ((null l) nil)
    ((listp (car l)) (cmmdc (cmmdcLista (car l)) (cmmdcLista (cdr l))))
    (t(cmmdc (car l) (cmmdcLista (cdr l))))
  )
)

; (print (cmmdcLista '(6 '(24 a b 6 (3) (9 c)) 12)))

(defun nrAparitii (l a)
  (cond
    ((null l) 0)
    ((listp (car l)) (+ (nrAparitii (car l) a) (nrAparitii (cdr l) a)))
    ((equal (car l) a) (+ (nrAparitii (cdr l) a) 1))
    (t(nrAparitii (cdr l) a))
  )
)

;(print (nrAparitii '(1 2 3 (2 3 ((1) 5) 6) 7 2) 2))

; 2. a) Definiti o functie care selecteaza al n-lea element al unei liste, sau
; NIL, daca nu exista.
; b) Sa se construiasca o functie care verifica daca un atom e membru al unei
; liste nu neaparat liniara.
; c) Sa se construiasca lista tuturor sublistelor unei liste. Prin sublista se
; intelege fie lista insasi, fie un element de pe orice nivel, care este
; lista. Exemplu: (1 2 (3 (4 5) (6 7)) 8 (9 10)) =>
; ( (1 2 (3 (4 5) (6 7)) 8 (9 10)) (3 (4 5) (6 7)) (4 5) (6 7) (9 10) ).
; d) Sa se scrie o functie care transforma o lista liniara intr-o multime.

(defun elemNAux (l n c)
  (cond
    ((null l) nil)
    ((= c n) (car l))
    (t(elemNAux (cdr l) n (+ c 1)))
  )
)
    
(defun elemN (l n)
  (elemNAux l n 1)
)
    
; (print (elemN '(1 2 3 4 5) 2))

(defun membru (l atom)
  (cond
    ((null l) nil)
    ((listp (car l)) (or (membru (car l) atom) (membru (cdr l) atom)))
    ((equal (car l) atom) t)
    (t(membru (cdr l) atom))
  )
)

; (print (membru '(1 2 3 (4 5 a (6 xd) 12 z) c) 'a))

(defun subliste (l)
  (cond
    ((atom l) nil)
    (T (APPLY #'APPEND (list l) (MAPCAR #'subliste l)))
  )
)

; (print (subliste '(1 2 3 (4 5 (6 7) (8 9) 10 11) 12 13)))

(defun stergeElem(l elem)
    (cond
        ((null l) nil)
        ((equal (car l) elem) (stergeElem (cdr l) elem))
        (t (append (list (car l)) (stergeElem (cdr l) elem)))
    )
)

(defun listaInMultime(l)
    (cond
        ((null l) nil)
        (t (cons (car l) (listaInMultime (stergeElem l (car l)))))
    )
)

; (print (listaInMultime '(1 2 3 1 2 4 5 1 4 5 6 7 8 4 5 8)))

; 3.a) Definiti o functie care intoarce produsul a doi vectori.
; b) Sa se construiasca o functie care intoarce adancimea unei liste.
; c) Definiti o functie care sorteaza fara pastrarea dublurilor o lista liniara.
; d) Sa se scrie o functie care intoarce intersectia a doua multimi.

(defun produsVectori(l1 l2)
  (cond
    ((null l1) 0)
    (t (+ (* (car l1) (car l2)) (produsVectori (cdr l1) (cdr l2))))
  )
)

; sau ig
;(defun produsVectori(l1 l2)
;  (cond
;    ((null l1) nil)
;    (t (cons (* (car l1) (car l2)) (produsVectori (cdr l1) (cdr l2))))
;  )
;)

;(print (produsVectori '(1 2 3) '(4 5 6)))

; b ez
; c sortare cu min ig si stergere

(defun intersectie (l1 l2)
  (cond
    ((null l1) nil)
    ((membru l2 (car l1)) (cons (car l1) (intersectie (cdr l1) l2)))
    (t(intersectie (cdr l1) l2))
  )
)

; (print (intersectie '(1 2 3 4 6) '(2 4 5 6 7)))

; b) Definiti o functie care obtine dintr-o lista data lista tuturor atomilor
; care apar, pe orice nivel, dar in aceeasi ordine. De exemplu:
; (((A B) C) (D E)) --> (A B C D E)

(defun totiAtomii (l)
  (cond
    ((null l) nil)
    ((listp (car l)) (append (totiAtomii (car l)) (totiAtomii (cdr l))))
    (t(cons (car l) (totiAtomii (cdr l))))
  )
)

; (print (totiAtomii '(((A B) C) (D E))))

; c) Sa se scrie o functie care plecand de la o lista data ca argument,
; inverseaza numai secventele continue de atomi. Exemplu:
; (a b c (d (e f) g h i)) ==> (c b a (d (f e) i h g))

(defun invSecvAux (l col)
  (cond
        ((null l) (reverse col))
        ((listp (car l)) (append (reverse col) (cons (invSecvAux (car l) nil) (invSecvAux (cdr l) nil))))
        (T (invSecvAux (cdr l) (append col (list (car l)))))
    )
)

(defun invSecv (l)
  (invSecvAux l nil)
)

; (print (invSecv '(a b c (d (e f) g h i))))

; b) Definiti o functie care substituie un element E prin elementele unei liste
; L1 la toate nivelurile unei liste date L.'

(defun substitutie (l el l1)
  (cond
    ((null l) nil)
    ((listp (car l)) (cons (substitutie (car l) el l1) (substitutie (cdr l) el l1)))
    ((equal (car l) el) (append l1 (substitutie (cdr l) el l1)))
    (t(cons (car l) (substitutie (cdr l) el l1)))
  )
)

; (print (substitutie '(1 2 3 (1 2 5 (1))) 1 '(a b)))

; b) Definiti o functie care determina succesorul unui numar reprezentat cifra
; cu cifra intr-o lista. De ex: (1 9 3 5 9 9) --> (1 9 3 6 0 0)

(defun succesorAux (l carry)
  (cond
    ((null l) nil)
    ((= carry 0) l)
    ((= (car l) 9) (cons '0 (succesorAux (cdr l) 1)))
    (t(cons (+ (car l) 1) (succesorAux (cdr l) 0)))
  )
)

(defun succesor (l)
  (cond
    ((= (car (reverse (succesorAux (reverse l) 1)) ) 0) (cons '1 (reverse (succesorAux (reverse l) 1))))
    (t(reverse (succesorAux (reverse l) 1)))
  )
)

; (print (succesor '(1 2 3 6 8)))

; b) Definiti o functie care inverseaza o lista impreuna cu toate sublistele
; sale de pe orice nivel.

(defun inverseaza (l)
  (cond
    ((null l) nil)
    ((listp (car l)) (append (inverseaza (cdr l)) (list (inverseaza (car l)))))
    (t(append (inverseaza (cdr l)) (list (car l))))
  )
)

; (print (inverseaza '(a b (c d e (f g h i) (j k l)))))

; L2
; 1. Sa se afiseze calea de la radacina pana la un nod x dat.

(defun cale (l nod)
  (cond
    ((null l) nil)
    ((equal (car l) nod) (list (car l)))
    ((membru (cadr l) nod) (cons (car l) (cale (cadr l) nod)))
    ((membru (caddr l) nod) (cons (car l) (cale (caddr l) nod)))
    (t nil)
  )
)

; (print (cale '(a (b (c (d) (e))) (f (h))) 'e))

; 2. Sa se tipareasca lista nodurilor de pe nivelul k 

(defun nivelAux (l k nivCur)
  (cond
    ((null l) nil)
    ((= k nivCur) (list (car l)))
    (t(append (nivelAux (cadr l) k (+ nivCur 1)) (nivelAux (caddr l) k (+ nivCur 1))))
  )
)

(defun nivel (l k)
  (nivelAux l k 1)
)

; (print (nivel '(a (b (c (d) (e))) (f (h) (i (j)))) 4))

; 3. Sa se precizeze numarul de niveluri din arbore
; ez


; 5. Sa se intoarca adancimea la care se afla un nod intr-un arbore

(defun adancimeAux (l nod niv)
  (cond
    ((null l) -1)
    ((equal (car l) nod) niv)
    (t(max (adancimeAux (cadr l) nod (+ 1 niv)) (adancimeAux (caddr l) nod (+ 1 niv))))
  )
)

(defun adancime (l nod)
  (adancimeAux l nod 1)
)

; (print (adancime '(a (b (c (d) (e))) (f (h) (i (j)))) 'h))

; 6.Sa se construiasca lista nodurilor unui arbore de parcurs in inordine

(defun inordine(l)
  (cond
    ((null l) nil)
    (t (append (inordine (cadr l)) (list (car l)) (inordine (caddr l))))
  )
)

; (print (inordine '(a (b (c (d) (e))) (f (h) (i (j)))) ))

; 11. Se da un arbore de tipul (2). Sa se afiseze nivelul (si lista corespunzatoare a nodurilor)
; avand numar maxim de noduri. Nivelul rad. se considera 0.
