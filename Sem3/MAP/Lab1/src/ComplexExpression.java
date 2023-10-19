enum Operation {
    ADDITION,
    SUBSTRACTION,
    MULTIPLICATION,
    DIVSION
}

public abstract class ComplexExpression {
    protected Operation operation;
    protected ComplexNumber[] args;

    public ComplexExpression(ComplexNumber[] args){
        this.args = args;
    }

    public final ComplexNumber execute(){
        ComplexNumber res = args[0];

        for (int i = 1; i < args.length; i++) {
            res = executeOneOperation(res, args[i]);
        }
        return res;
    }

    public abstract ComplexNumber executeOneOperation(ComplexNumber res, ComplexNumber c);
}

class AddComplexExpression extends ComplexExpression {
    public AddComplexExpression(ComplexNumber[] args){
        super(args);
        this.operation = Operation.ADDITION;
    }

    @Override
    public ComplexNumber executeOneOperation(ComplexNumber res, ComplexNumber c){
        return res.add(c);
    }
}

class SubtractComplexExpression extends ComplexExpression {
    public SubtractComplexExpression(ComplexNumber[] args){
        super(args);
        this.operation = Operation.SUBSTRACTION;
    }

    @Override
    public ComplexNumber executeOneOperation(ComplexNumber res, ComplexNumber c){
        return res.subtract(c);
    }
}

class MultiplyComplexExpression extends ComplexExpression {
    public MultiplyComplexExpression(ComplexNumber[] args){
        super(args);
        this.operation = Operation.MULTIPLICATION;
    }

    @Override
    public ComplexNumber executeOneOperation(ComplexNumber res, ComplexNumber c){
        return res.multiply(c);
    }
}

class DivideComplexExpression extends ComplexExpression {
    public DivideComplexExpression(ComplexNumber[] args){
        super(args);
        this.operation = Operation.DIVSION;
    }

    @Override
    public ComplexNumber executeOneOperation(ComplexNumber res, ComplexNumber c){
        return res.divide(c);
    }
}