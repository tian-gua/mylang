package lexer;

public abstract class Token {

    public static final Token  EOF = new Token(-1) {
        @Override
        public Object value() {
            return null;
        }
    };
    public static final String EOL = "\\n";

    protected Integer line;

    public abstract Object value();

    public Token(Integer line) {
        this.line = line;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }
}
