function [g_nodes,g_coeff]=Gaussquad(alpha,beta)
    % Generate Gaussian quadrature formula. Computes nodes and coefficients
    % for Gauss rules given alpha and beta, with Jacobi matrix
    
    n=length(alpha); rb=sqrt(beta(2:n));
    J=diag(alpha)+diag(rb,-1)+diag(rb,1);
    [v,d]=eig(J);
    g_nodes=diag(d); 
    g_coeff=beta(1)*v(1,:).^2;
end