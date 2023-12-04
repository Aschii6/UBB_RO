; Se da un arbore de tipul (1). Sa se precizeze nivelul pe care apare un nod
; x in arbore. Nivelul radacii se considera a fi 0. 

(defun parcurg_st(arb nv nm)
  (cond
    ((null arb) nil)
    ((= nv (+ 1 nm)) nil)
    (t (cons (car arb) (cons (cadr arb) (parcurg_st (cddr arb) (+ 1 nv) (+ (cadr arb) nm)))))
  )
)

(defun stang(arb)
  (parcurg_st (cddr arb) 0 0)
)

(defun parcurg_dr(arb nv nm)
  (cond
    ((null arb) nil)
    ((= nv (+ 1 nm)) arb)
    (t (parcurg_dr (cddr arb) (+ 1 nv) (+ (cadr arb) nm)))
  )
)

(defun drept(arb)
  (parcurg_dr (cddr arb) 0 0)
)

(defun nivel_aux(arb nod nivelCurent)
  (cond
    ((null arb) -1)
    ((eql (car arb) nod) nivelCurent)
    (t (max (nivel_aux (stang arb) nod (+ 1 nivelCurent)) (nivel_aux (drept arb) nod (+ 1 nivelCurent))))
  )
)

(defun nivel(arb nod)
  (nivel_aux arb nod 0)
)

(print (nivel '(a 2 b 1 c 0 d 1 e 0) 'b))
