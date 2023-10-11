public class ExpressionFactory {
    private static final ExpressionFactory expressionFactory = new ExpressionFactory();

    private ExpressionFactory(){}

    public static ExpressionFactory getInstance(){
        return expressionFactory;
    }

    public ComplexExpression createExpression(Operation operation, ComplexNumber[] args){
        return switch (operation){
            case ADDITION -> new AddComplexExpression(args);
            case SUBSTRACTION -> new SubtractComplexExpression(args);
            case MULTIPLICATION -> new MultiplyComplexExpression(args);
            case DIVSION -> new DivideComplexExpression(args);
        };
    }
}
