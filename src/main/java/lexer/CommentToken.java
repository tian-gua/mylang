package lexer;

public class CommentToken extends Token {

    private String comment;

    public CommentToken(Integer line, String comment) {
        super(line);
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CommentToken{" +
                "comment='" + comment + '\'' +
                ", line=" + line +
                '}';
    }

    @Override
    public String value() {
        return comment;
    }
}
