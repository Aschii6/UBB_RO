; pune de n ori 0 in fata listei
; l - lista data
; n - nr de 0
(defun umpleCu0 (l n)
  (cond
    ((= n 0) l)
    (t (cons 0 (umpleCu0 l (- n 1))))
  )
)

; l1 - prima lista din adunare
; l2 - a doua lista din adunare
; carry - carry-ul obtinut din adunarea cifrelor de acelasi ordin
(defun suma (l1 l2 carry)
  (cond
    ((null l1) nil)
    ((> (+ (car l1) (car l2) carry) 9) (cons (mod (+ (car l1) (car l2) carry) 10) (suma (cdr l1) (cdr l2) 1)))
    (t(cons (+ (car l1) (car l2) carry) (suma (cdr l1) (cdr l2) 0)))
  )
)

; l1 - prima lista din adunare
; l2 - a doua lista din adunare
(defun sumaMainAux (l1 l2)
  (cond
    ((= (car (reverse (suma (reverse l1) (reverse l2) 0))) 0) (cdr (reverse (suma (reverse l1) (reverse l2) 0))))
    (t (reverse (suma (reverse l1) (reverse l2) 0)))
  )
)

; l1 - prima lista din adunare
; l2 - a doua lista din adunare
(defun sumaMain (l1 l2)
  (cond
    ((> (length l1) (length l2)) (sumaMainAux (cons 0 l1) (cons 0 (umpleCu0 l2 (- (length l1) (length l2))))))
    (t (sumaMainAux (cons 0 l2) (cons 0 (umpleCu0 l1 (- (length l2) (length l1)))))) 
  )
)

(defun my_reverse (l)
  (cond
    ((null l) nil)
    (t (append (cdr l) (list (car l))))
  )
)

(print (sumaMain '(1 1 1) '(2 2 2)))

(print (sumaMain '(1 2 3 4) '(1 2 3)))

(print (sumaMain '(1 2 8) '(3 5)))

(print (sumaMain '(8 8) '(5 7)))

(print (sumaMain '(9 9) '(9)))