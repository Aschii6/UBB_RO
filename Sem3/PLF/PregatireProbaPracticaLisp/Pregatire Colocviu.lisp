; Se consideră o listă neliniară. Să se scrie o funcţie LISP care să aibă ca rezultat lista iniţială 
; în care toate apariţiile unui element e au fost înlocuite cu o valoare e1. Se va folosi o funcție MAP.
; Exemplu a) dacă lista este (1 (2 A (3 A)) (A)) e este A şi e1 este B => (1 (2 B (3 B)) (B))
; b) dacă lista este (1 (2 (3))) şi e este A => (1 (2 (3)))

(defun inloc (l e e1)
  (cond
    ((eq l e) e1)
    ((atom l) l)
    (t (mapcar #'(lambda (x) (inloc x e e1)) l))
  )
)

;(print (inloc '(1 (2 A (3 A)) (A)) 'A 'B))

; Să se substituie un element  e prin altul e1 la orice nivel impar al unei liste neliniare
; Nivelul superficial se consideră 1. 
; De exemplu, pentru lista (1 d (2 d (d))), e=d şi e1=f rezultă lista (1 f (2 d (f))).

(defun inloc2 (l e e1 niv)
  (cond
    ((and (eq l e) (oddp niv)) e1)
    ((atom l) l)
    (t (mapcar #'(lambda (x) (inloc2 x e e1 (+ 1 niv))) l))
  )
)

;o functie main
;(print (inloc2 '(1 d (2 d (d))) 'd 'f 0))

;  Un arbore n-ar se reprezintă în LISP astfel (nod subarbore1 subarbore2 .....). 
; Se cere să se verifice dacă un nod x apare pe un nivel par în arbore. 
; Nivelul rădăcinii se consideră a fi 0. Se va folosi o funcție MAP.
; Exemplu pentru arborele (a (b (g)) (c (d (e)) (f)))
; a) x=g => T b) x=h => NIL

(defun apareNivPar (l x niv)
  (cond
    ((and (evenp niv) (eq l x)) t)
    ((atom l) nil)
    (t (sau (mapcar #'(lambda (l) (apareNivPar l x (+ 1 niv))) l)))
  )
)
      
(defun sau (l)
  (cond
    ((null l) nil)
    ((eq (car l) t) t)
    (t (sau (cdr l)))
  )
)

;(print (apareNivPar '(a (b (g)) (c (d (e)) (f))) 'g -1))

; D. Un arbore n-ar se reprezintă în LISP astfel ( nod subarbore1 subarbore2 .....). 
; Se cere să se determine calea de la radăcină către un nod dat. Se va folosi o funcție MAP.
; Exemplu pentru arborele (a (b (g)) (c (d (e)) (f)))
; a) nod=e => (a c d e) 
; b) nod=v => ()

(defun apartine(l e)
  (cond
    ((null l) nil)
    ((eq (car l) e) t)
    ((listp (car l)) (or (apartine (car l) e) (apartine (cdr l) e)))
    (t (apartine (cdr l) e))
  )
)

; cam naspa + trebuie apelat idefk

(defun membru (l e)
  (cond
    ((and (atom l) (eq l e)) t)
    ((atom l) nil)
    (t (sau (mapcar #'(lambda (x) (membru x e)) l)))
  )
)

(defun drum (l e)
  (cond
    ((atom l) nil)
    ((membru l e) (cons (car l) (apply #'append (mapcar #'(lambda (x) (drum x e)) l))))
  )
)

; (print (drum '(a (b (g)) (c (d (e)) (f)) ) 'e))

; Se consideră o listă neliniară. Să se scrie o funcţie care să aibă ca rezultat lista iniţială in care atomii 
; de pe nivelul k au fost înlocuiți cu 0 (nivelul superficial se consideră 1). Se va folosi o funcție MAP.
; Exemplu pentru lista (a (1 (2 b)) (c (d)))
; a) k=2 => (a (0 (2 b)) (0 (d))) b) k=1 => (0 (1 (2 b)) (c (d))) c) k=4 =>lista nu se modifică

(defun inloc3 (l niv k)
  (cond
    ((and (= niv k) (atom l)) 0)
    ((atom l) l)
    (t (mapcar #'(lambda (x) (inloc3 x (+ 1 niv) k)) l))
  )
)

; functie main
; (print (inloc3 '(a (1 (2 b)) (c (d))) 0 2))

; D. Să se substituie valorile numerice cu o valoare e dată, la orice nivel al unei liste neliniare. 
; Se va folosi o funcție MAP.
; Exemplu, pentru lista (1 d (2 f (3))), e=0 rezultă lista (0 d (0 f (0))).

(defun inloc4 (l e)
  (cond
    ((numberp l) e)
    ((atom l) l)
    (t (mapcar #'(lambda (x) (inloc4 x e)) l))
  )
)

; (print (inloc4 '(1 d (2 f (3))) 0))

; D. Se consideră o listă neliniară. Să se scrie o funcţie LISP care să aibă ca rezultat lista iniţială 
; din care au fost eliminaţi toţi atomii nenumerici de pe nivelurile pare (nivelul superficial se consideră 1). 
; Se va folosi o funcție MAP. Exemplu pentru lista (a (1 (2 b)) (c (d))) rezultă (a (1 (2 b)) ((d)))

(defun elimina (l niv)
  (cond
    ((numberp l) (list l))
    ((and (atom l) (evenp niv)) nil)
    ((atom l) (list l))
    (t (list (apply #'append (mapcar #'(lambda (x) (elimina x (+ 1 niv))) l))))
  )
)

; wrapper
; (print (elimina '(a (1 (2 b)) (c (d))) 0))

; D. Un arbore n-ar se reprezintă în LISP astfel ( nod subarbore1 subarbore2 .....). Se cere să se determine 
; înălțimea unui nod în arbore. Se va folosi o funcție MAP.
; Exemplu pentru arborele (a (b (g)) (c (d (e)) (f)))
; a) nod=e => înălțimea e 0 b) nod=v => înălțimea e -1 c) nod=c => înălțimea e 2

(defun inaltime (l elem gasit)
  (cond
    ((atom l) -1)
    ((and (listp l) (eq gasit nil) (not (eq (car l) elem))) (apply #'max (mapcar #'(lambda (x) (inaltime x elem nil)) l)))
    (t (+ 1 (apply #'max (mapcar #'(lambda (x) (inaltime x elem t)) l))))
  )
)

(print (inaltime '(a (b (g)) (c (d (e)) (f))) 'a nil))

; D. Un arbore n-ar se reprezintă în LISP astfel ( nod subarbore1 subarbore2 .....). 
; Se cere să se determine lista nodurilor de pe nivelurile pare din arbore (în ordinea nivelurilor 0, 2, …). 
; Nivelul rădăcinii se consideră 0. Se va folosi o funcție MAP.
; Exemplu pentru arborele (a (b (g)) (c (d (e)) (f))) => (a g d f)

(defun nivPar (l niv)
  (cond
    ((and (atom l) (oddp niv)) nil) ; se pastreaza de la niv par xd
    ((atom l) (list l))
    (t (apply #'append (mapcar #'(lambda (x) (nivPar x (+ 1 niv))) l)))
  )
)

; functie main
; (print (nivPar '(a (b (g)) (c (d (e)) (f))) -1))

; D. Un arbore n-ar se reprezintă în LISP astfel (nod subarbore1 subarbore2 .....). Se cere să se determine 
; numărul de noduri de pe nivelul k. Nivelul rădăcinii se consideră 0. Se va folosi o funcție MAP.
; Exemplu pentru arborele (a (b (g)) (c (d (e)) (f))) a) k=2 => nr=3 (g d f) b) k=4 => nr=0 () 

(defun nrNoduriNiv (l niv k)
  (cond
    ((and (atom l) (= niv k)) 1)
    ((atom l) 0)
    (t (apply #'+ (mapcar #'(lambda (x) (nrNoduriNiv x (+ 1 niv) k)) l)))
  )
)

; (print (nrNoduriNiv '(a (b (g)) (c (d (e)) (f))) -1 2))

