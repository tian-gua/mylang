package lexer;

public class BoolToken extends Token {

    private Boolean b;

    public BoolToken(Integer line, Boolean b) {
        super(line);
        this.b = b;
    }

    @Override
    public Object value() {
        return b;
    }

    public Boolean getB() {
        return b;
    }

    public void setB(Boolean b) {
        this.b = b;
    }
}
