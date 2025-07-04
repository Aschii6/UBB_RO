function [x, ni]=sor(A, b, omega, x0, err, nitmax)
    % Rezolva sistemul Ax = b, pentru x, folosind metoda iterativa 
    % Successive Over-Relaxation 
    % A - matricea sistemului
    % b - matrice rhs - termeni liberi
    % omega - parametrul de relaxare (intre 0 si 2)
    % x0 - vectorul de la care incepe sa se construiasca solutia
    % err - toleranta dorita (criteriu de oprire)
    % nitmax - numarul maxim de iteratii
    % Returneaza: x - solutia, ni - numarul de iteratii necesare
    
    % Niste valori default
    if nargin < 6, nitmax=50; end
    if nargin < 5, err=1e-3; end
    if nargin < 4, x0=zeros(size(b)); end
    
    if omega <= 0 || omega >= 2
        error('Relaxation parameter incorrect')
    end
    
    [m,n] = size(A);
    if (m ~= n) || ( n~= length(b))
        error('Incorrect sizes')
    end
    
    M= 1 / omega * diag(diag(A)) + tril(A, -1);
    N = M - A;
    x = x0;
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