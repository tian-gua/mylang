package lexer;

public class UnknownToken extends Token {

    private String source;

    public UnknownToken(Integer line, String source) {
        super(line);
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String value() {
        return source;
    }
}

