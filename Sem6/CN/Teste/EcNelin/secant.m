function [z,ni]=secant(f,x0,x1,ea,er,Nmax,varargin)
    % Metoda secantei pentru ecuatii in R
    % f - functia
    % x0,x1 - valori de pornire
    % ea,er - eroarea absoluta, resp eroarea relativa
    % Nmax - numarul maxim de iteratii
    % z - aproximatia radacinii
    % ni - numar de iteratii

    if nargin<6, Nmax=50; end
    if nargin<5, er=0; end
    if nargin<4, ea=1e-3; end
    
    xv=x0; fv=f(xv,varargin{:}); xc=x1; fc=f(xc,varargin{:});
    for k=1:Nmax
        xn=xc-fc*(xc-xv)/(fc-fv);
        %[xn-xc,sign(xn-xc)]
        if abs(xn-xc)<ea+er*xn
            z=xn;
            ni=k;
            return
        end
        % pregatire iteratia urmatoare
        xv=xc; fv=fc; xc=xn; fc=f(xn,varargin{:});
    end
    
    error('numarul maxim de iteratii depasit')
end