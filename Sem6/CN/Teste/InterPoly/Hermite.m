function ff = Hermite(x, y, yd, xx)
    % Calculates the Hermite interpolation
    % x - nodes
    % y - function
    % yd - function derivative
    % xx - evaluation points

    [xd, T] = divdiffdn(x, y, yd);

    % Newtonpol
    lt = length(xx); lx = length(xd);
    for j = 1:lt
       d = xx(j) - xd;
       ff(j)=[1, cumprod(d(1:lx-1))] * T(1,:)';
    end
end