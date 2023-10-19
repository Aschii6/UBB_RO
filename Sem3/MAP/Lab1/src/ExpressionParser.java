import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class ExpressionParser {
    public ExpressionParser(){

    }
    public ComplexExpression parse(String[] args){
        String semn = args[1];

        Operation operation;

        if (!semn.equals("+") && !semn.equals("-") && !semn.equals("*") && !semn.equals("/"))
            return null;

        operation = switch (semn) {
            case "+" -> Operation.ADDITION;
            case "-" -> Operation.SUBSTRACTION;
            case "*" -> Operation.MULTIPLICATION;
            default -> Operation.DIVSION;
        };

        for (int i = 1; i < args.length; i = i + 2)
            if (!Objects.equals(args[i], semn))
                return null;

        ArrayList<ComplexNumber> arrayList = new ArrayList<ComplexNumber>();

        for (int i = 0; i < args.length; i = i + 2){
            Pattern p = Pattern.compile("-?\\d+");
            Matcher m = p.matcher(args[i]);
            String real = "0";
            String imag = "0";
            if(m.find()){
                real = m.group(0);
                if(m.find()){
                    imag = m.group(0);
                }
            }
            arrayList.add(new ComplexNumber(Float.parseFloat(real), Float.parseFloat(imag)));
        }
        ComplexNumber[] numbers = new ComplexNumber[arrayList.size()];

        for (int j = 0; j < arrayList.size(); j++)
            numbers[j] = arrayList.get(j);

        return ExpressionFactory.getInstance().createExpression(operation, numbers);
    }
}

