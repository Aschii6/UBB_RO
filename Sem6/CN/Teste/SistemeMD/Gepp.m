% A - matrice
% b - vector rhs
function [x, flops] = Gepp(A, b)
    [~, n] = size(A);
    x = zeros(size(b));
    A = [A, b];
    flops = 0;

    for i = 1:n-1
        [u, p] = max(abs(A(i:n, i)));
        p = p + i - 1;
        
        if u == 0
            error('no unique solution')
        end

        if p ~= i
            % Interschimbare de linii
            A([i, p], i:n+1) = A([p, i], i:n+1);
        end

        % Se calculeaza multiplicatorii
        j = i+1:n;
        m = A(j, i) / A(i, i); flops = flops + n - i;
        A(j, i+1:n+1) = A(j, i+1:n+1) - m * A(i, i+1:n+1); flops = flops + 2 * (n - i + 1) * (n - i);
    end

    % backsubst
    x(n) = A(n, n+1) / A(n, n); flops = flops + 1;
    for i = n-1:-1:1
        x(i) = (A(i, n+1) - A(i, i+1:n) * x(i+1:n)) / A(i,i); flops = flops + 2 * (n - i) + 1;
    end
    
end