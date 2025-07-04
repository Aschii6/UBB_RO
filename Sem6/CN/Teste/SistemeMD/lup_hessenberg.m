% Descompunere LUP a unei matrici Hessenberg
% A - matrice Hessenberg pentru a fi descompusa
function [L, U, P] = lup_hessenberg(A)
    [m, n] = size(A);
    P = zeros(m, n);
    piv = (1:m)'; % vector de permutari
    
    for i = 1:m-1
        % Pivot
        [~, kp] = max(abs([A(i,i), A(i+1, i)]));
        kp = kp + i - 1;
        
        % Interschimbare de linii
        if i ~= kp
            A([i,kp], :) = A([kp,i], :);
            piv([i, kp]) = piv([kp, i]);
        end
        
        A(i+1, i) = A(i+1, i) / A(i, i);
        A(i+1, i+1:n) = A(i+1, i+1:n) - A(i+1, i) * A(i, i+1:n);
    end
    for i = 1:m
        P(i, piv(i)) = 1;
    end
    U = triu(A);
    L = tril(A, -1) + eye(m);
end