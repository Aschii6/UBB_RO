function [z, ni]=gauss_seidel(A, b, x0, err, nitmax)
    % Rezolva sistemul Ax = b, pentru x, folosind metoda iterativa a lui
    % Gauss-Seidel
    % A - matricea sistemului
    % b - matrice rhs - termeni liberi
    % x0 - vectorul de la care incepe sa se construiasca solutia
    % err - toleranta dorita (criteriu de oprire)
    % nitmax - numarul maxim de iteratii
    % Returneaza: z - solutia, ni - numarul de iteratii necesare
    
    % Niste valori default
    if nargin < 5, nitmax = 50; end
    if nargin < 4, err = 1e-3; end
    if nargin < 3, x0 = zeros(size(b)); end
    
    [m, n] = size(A);
    
    if (m ~= n) || (n ~= length(b))
        error('Incorrect sizes')
    end
    
    x = x0(:);
    
    M = tril(A);
    N = M - A;
    for i = 1:nitmax
        x0 = x;
        x = M \ (N * x0 + b);
        if norm(x - x0, inf)< err * norm(x, inf)
            z = x;
            ni = i;
            return
        end
    end
    
    z = x;
    ni = nitmax;
    warning('Max iterations reached')
end