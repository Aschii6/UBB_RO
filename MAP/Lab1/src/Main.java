public class Main {
    public static void main(String[] args) {
        /*for (String arg : args)
            System.out.println(arg);*/

        ExpressionParser expressionParser = new ExpressionParser();

        ComplexExpression complexExpression = expressionParser.parse(args);

        if (complexExpression != null){
            ComplexNumber result = complexExpression.execute();

            System.out.println(result);
        }
    }
}