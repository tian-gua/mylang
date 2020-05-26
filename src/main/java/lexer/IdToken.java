package lexer;

public class IdToken extends Token {

    private String id;

    public IdToken(Integer line, String id) {
        super(line);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "IdToken{" +
                "id='" + id + '\'' +
                ", line=" + line +
                '}';
    }

    @Override
    public String value() {
        return id;
    }
}
