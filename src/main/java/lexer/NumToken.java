package lexer;

public class NumToken extends Token {

    private Integer num;

    public NumToken(Integer line, Integer num) {
        super(line);
        this.num = num;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "NumToken{" +
                "num=" + num +
                ", line=" + line +
                '}';
    }

    @Override
    public Integer value() {
        return num;
    }
}
