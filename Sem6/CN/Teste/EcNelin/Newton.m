function [z,ni]=Newton(f,fd,x0,ea,er,nmax)
    % Metoda lui Newton pentru ecuatii neliniare in R si R^n
    % f - functia
    % fd - derivata
    % x0 - punct de inceput
    % ea - eroarea absoluta
    % er - eroarea relativa
    % nmax - numarul maxim de iteratii
    % z - aproximatia radacinii
    % ni - numarul de iteratii

    if nargin < 6, nmax=60; end
    if nargin < 5, er=0; end
    if nargin < 4, ea=1e-3; end
    
    xp=x0(:); % garantie vector coloana
    for k=1:nmax
        xc=xp-fd(xp)\f(xp);
        if norm(xc-xp,inf)<ea+er*norm(xc,inf)
            z=xc;
            ni=k;
            return
        end
        xp=xc;
    end
    error('S-a depasit numarul maxim de iteratii');
end