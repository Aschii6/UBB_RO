function [c1, c2]=cond_quad(a, b, c)
    r = roots([a, b, c]);
    x1 = r(1); x2 = r(2);
    
    dp = @(x) 2*a*x + b;
    
    c1 = (abs(b * x1) + abs(c)) / abs(x1 * dp(x1));
    c2 = (abs(b * x2) + abs(c)) / abs(x1 * dp(x2));
end