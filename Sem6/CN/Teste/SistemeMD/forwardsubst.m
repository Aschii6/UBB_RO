% L - matrice triunghiulara inferior
% b - vector rhs
function x = forwardsubst(L, b)
    n = length(b);
    x = zeros(size(b));
    
    for k = 1:n
        x(k) = (b(k) - L(k, 1:k-1) * x(1:k-1)) / L(k, k);
    end
end