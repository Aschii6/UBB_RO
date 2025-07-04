function [x,ni]=jacobi(A,b,x0,err,nitmax)
    % Rezolva sistemul Ax = b, pentru x, folosind metoda iterativa a lui
    % Jacobi
    % A - matricea sistemului
    % b - matrice rhs - termeni liberi
    % x0 - vectorul de la care incepe sa se construiasca solutia
    % err - toleranta dorita (criteriu de oprire)
    % nitmax - numarul maxim de iteratii
    % Returneaza: x - solutia, ni - numarul de iteratii necesare
    
    % Niste valori default
    if nargin < 5, nitmax=50; end
    if nargin < 4, err=1e-3; end
    if nargin < 3, x0=zeros(size(b)); end
    
    [m, n] = size(A);
    
    if (m ~= n) || (n ~= length(b))
        error('Incorrect sizes')
    end
    
    M = diag(diag(A));
    N = M - A;
    
    x = x0(:);
    
    for i = 1:nitmax
        x0 = x;
        x = M \ (N * x0 + b);
        if norm(x - x0, inf) < err * norm(x, inf)
            ni = i;
            return
        end
    end
    
    ni = nitmax;  
    warning('Max iterations reached')
end