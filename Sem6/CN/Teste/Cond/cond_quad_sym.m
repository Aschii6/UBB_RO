function cond_root = cond_quad_sym()
    syms a b c x real
    assume(a ~= 0)  % Trebuie sa fie ecuatie de gradul 2

    % Polinom si derivata sa
    p = a*x^2 + b*x + c;
    dp = diff(p, x);

    % Radacinile simbolice
    S = solve(p, x, 'ReturnConditions', true);
    r = S.x;
    
    % Calcul condiționare pentru fiecare radacina
    cond_root = sym('cond', [1 2]);
    for i = 1:2
        xi = r(i);
        dpi = subs(dp, x, xi);
        cond_root(i) = simplify(1 / abs(xi * dpi) * ...
            (abs(a)*abs(xi)^2 + abs(b)*abs(xi) + abs(c)));
    end

    cond_root = simplify(cond_root, 'Steps', 50);
    
    % Salvez numarul de conditionare ca o functie
    % matlabFunction(cond_root, 'Vars', [a, b, c], 'File', 'cond_quad_eval');
end
