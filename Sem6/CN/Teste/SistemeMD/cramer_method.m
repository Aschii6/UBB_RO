function [x, flops] = cramer_method(A, B, n)
    flops = 0;
    if n == 2
        x = zeros(2, 1);

        [detA, f] = determinant(2, A); flops = flops + f;
        [detX1, f] = determinant(2, [B, A(:, 2)]); flops = flops + f;
        [detX2, f] = determinant(2, [A(:, 1), B]); flops = flops + f;

        x(1) = detX1 / detA; flops = flops + 1;
        x(2) = detX2 / detA; flops = flops + 1;
    end

    if n == 3
        x = zeros(3, 1);

        [detA, f] = determinant(3, A); flops = flops + f;
        [detX1, f] = determinant(3, [B, A(:, 2), A(:, 3)]); flops = flops + f;
        [detX2, f] = determinant(3, [A(:, 1), B, A(:, 3)]); flops = flops + f;
        [detX3, f] = determinant(3, [A(:, 1), A(:, 2), B]); flops = flops + f;

        x(1) = detX1 / detA; flops = flops + 1;
        x(2) = detX2 / detA; flops = flops + 1;
        x(3) = detX3 / detA; flops = flops + 1;
    end
end

