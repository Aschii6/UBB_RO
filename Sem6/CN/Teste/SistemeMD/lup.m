% A - matrice
function [L,U,P]=lup(A)
    [m,n]=size(A);
    P=zeros(m,n);
    piv=(1:m)'; % vector de permutari
    
    for i=1:m-1
        % Pivot
        [~,kp]=max(abs(A(i:m,i)));
        kp=kp+i-1;
        
        % Interschimbare de linii
        if i~=kp
            A([i,kp],:)=A([kp,i],:);
            piv([i,kp])=piv([kp,i]);
        end
        
        % Complement Schur
        lin=i+1:m;
        A(lin,i)=A(lin,i)/A(i,i);
        A(lin,lin)=A(lin,lin)-A(lin,i)*A(i,lin);
    end
    
    for i=1:m
        P(i,piv(i))=1;
    end
    
    U=triu(A);
    L=tril(A,-1)+eye(m);
end