function omega=relopt(A)
    % Calculeaza parametrul de relaxare ideal al unei matrici de sistem

    M=diag(diag(A));
    N=M-A;
    T=M\N;
    
    if issparse(T)
        e=eigs(T, 4);
    else
        e=eig(T);
    end
    
    rt=max(abs(e));
    omega=2/(1+sqrt(1-rt^2));
end