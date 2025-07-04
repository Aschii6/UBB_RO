function z=evalsplinec(x,c,t)
    % Compute cubic spline value
    % t - evaluation points 
    % x - nodes (knots)
    % c - coefficients
    n=length(x);
    x=x(:); t=t(:);
    k = ones(size(t));
    for j = 2:n-1
        k(x(j) <= t) = j;
    end
    
    % interpolant evaluation
    s = t - x(k);
    %z = d(k) + s.*(c(k) + s.*(b(k) + s.*a(k)));
    z = c(k,4) + s.*(c(k,3) + s.*(c(k,2) + s.*c(k,1)));
end
