package lexer;

public class StringToken extends Token {

    private String text;

    public StringToken(Integer line, String text) {
        super(line);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "StringToken{" +
                "text='" + text + '\'' +
                ", line=" + line +
                '}';
    }

    @Override
    public String value() {
        return text;
    }
}
