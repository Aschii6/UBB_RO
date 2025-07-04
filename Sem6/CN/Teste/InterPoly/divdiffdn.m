function [z,td]=divdiffdn(x, f, fd)
    % double nodes
    % x noduri
    % f valorile nodurilor in f
    % fd valorile nodurilor in derivata lui f
    
    z = zeros(1, 2*length(x));
    lz=length(z);
    z(1:2:lz-1)=x;
    z(2:2:lz)=x;
    td=zeros(lz, lz);
    td(1:2:lz-1,1)=f';
    td(2:2:lz,1)=f';
    td(1:2:lz-1,2)=fd';
    td(2:2:lz-2,2) = (diff(f)./diff(x))';
    
    for j=3:lz
        td(1:lz-j+1,j)=diff(td(1:lz-j+2,j-1))./(z(j:lz)-z(1:lz-j+1))';
    end
end