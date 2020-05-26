package lexer;

public class BraceToken extends Token {
    private String text;

    public BraceToken(Integer line, String text) {
        super(line);
        this.text = text;
    }

    @Override
    public Object value() {
        return text;
    }

    @Override
    public String toString() {
        return "BraceToken{" +
                "text='" + text + '\'' +
                ", line=" + line +
                '}';
    }
}
