function roots = stable_quadratic_solver(a, b, c)
    % Rezolva ecuatia ax^2 + bx + c = 0 in mod stabil.
    
    % Precizia cvadrupla cu vpa, 34 cifre decimale, 113 biti 
    digits(34); 

    % Calculam determinantul cu vpa
    a_vpa = vpa(a);
    b_vpa = vpa(b);
    c_vpa = vpa(c);
    delta_vpa = b_vpa^2 - 4*a_vpa*c_vpa;
    sqrt_delta_vpa = sqrt(delta_vpa);
    
    sqrt_D = double(sqrt_delta_vpa);  % Truncare la precizie dubla
    
    if abs(sqrt_D) < 1e-6 % b^2 aprox. egal cu 4*a*c
        % Doua radacini egale
        x1 = -b / (2*a);
        x2 = x1;
    else
        % Doua radacini diferite
        if log(b) > (log(a) + log(c)) / 2 % b^2 >> 4ac
            if b >= 0
                x1 = (2*c) / (-b - sqrt_D);
                x2 = (-b - sqrt_D) / (2*a);
            else
                x1 = (-b + sqrt_D) / (2*a);
                x2 = (2*c) / (-b + sqrt_D);
            end
        else % calcul normal
            x1 = (-b + sqrt_D) / (2*a);
            x2 = (-b - sqrt_D) / (2*a);
        end
    end

    roots = [x1, x2];
end
