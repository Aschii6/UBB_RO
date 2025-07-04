function ff=barycentricInterpolation(x,y,xx,c)
    % Barycentric Lagrange interpolation
    % x - nodes
    % y - function values
    % xx - interpolation points
    % c - barycentric weights

    n=length(x)-1;
    numer = zeros(size(xx));
    denom = zeros(size(xx));
    exact = zeros(size(xx));
    
    for j=1:n+1
        xdiff = xx - x(j);
        temp = c(j)./xdiff;
        numer = numer + temp*y(j);
        denom = denom + temp;
        exact(xdiff==0) = j;
    end
    ff = numer ./ denom;
    jj = find(exact); 
    ff(jj) = y(exact(jj));
end