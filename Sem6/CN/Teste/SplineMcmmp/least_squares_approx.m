function res = least_squares_approx(x, y, functions, points)
    phi = functions(x);
    phi_approx = functions(points);
    
    [n, ~] = size(phi);

    A = zeros(n, n);
    B = zeros(n, 1);

    % Compute A = Z^T * Z ; B = Z^T * y ; where Z^T is phi
    for i = 1:n
        for j = 1:n
            A(i, j) = phi(i, :) * phi(j, :)';
        end
        B(i) = phi(i, :) * y';
    end

    a = A \ B;

    res = a' * phi_approx;
end
