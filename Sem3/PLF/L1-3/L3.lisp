; L3
; Sa se scrie o functie care sterge toate aparitiile unui atom de la
; toate nivelurile unei liste.

(defun stergeAtom (l a)
  (cond
    ((and (atom l) (eql l a)) nil)
    ((atom l) (list l))
    (t (list (APPLY #'append (MAPCAR #'(LAMBDA (x) (stergeAtom x a)) l))))
  )
)

(defun stergeAtomMain (l a)
  (car (stergeAtom l a))
)

(print (stergeAtomMain '(1 2 (1 3 4 (1 (1 2)) 6 7) 1) '1))