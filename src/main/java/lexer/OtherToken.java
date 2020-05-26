package lexer;

public class OtherToken extends Token {

    private String text;

    public OtherToken(Integer line, String text) {
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
        return "OtherToken{" +
                "text='" + text + '\'' +
                ", line=" + line +
                '}';
    }

    @Override
    public String value() {
        return text;
    }
}
