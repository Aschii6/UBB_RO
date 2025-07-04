function [result, flops] = determinant(n, A)
    if n == 2
        result = A(1,1) * A(2, 2) - A(1, 2) * A(2, 1);
        flops = 3;  % 3 flops
    end

    if n == 3
        result = A(1, 1) * (A(2, 2) * A(3, 3) - A(2, 3) * A(3, 2)) - ...
            A(1, 2) * (A(2, 1) * A(3, 3) - A(2, 3) * A(3, 1)) + ...
            A(1, 3) * (A(2, 1) * A(3, 2) - A(2, 2) * A(3, 1));
        flops = 14; % 14 flops
    end
end