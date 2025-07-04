function fi=Lagrange(x,y,xi)
    % x - nodes
    % y - function values
    % xi - values of Lagrange interpolation polynomial

	if nargin ~= 3
		error('')
	end

	[mu, nu]=size(xi);
	fi=zeros(mu,nu);
	np1=length(y);
	for i=1:np1
		z=ones(mu,nu);
		for j=[1:i-1,i+1:np1]
			z=z.*((xi-x(j))/(x(i)-x(j)));
		end
		fi=fi+z*y(i);
	end
end