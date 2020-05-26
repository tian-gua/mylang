package lexer;

public class OperatorToken extends Token {

    private String operator;

    public OperatorToken(Integer line, String operator) {
        super(line);
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "OperatorToken{" +
                "operator='" + operator + '\'' +
                ", line=" + line +
                '}';
    }

    @Override
    public String value() {
        return operator;
    }
}
