package lexer;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    public static final String SPACE          = "\\s*";
    public static final String COMMENT        = "(//.*)";
    public static final String NUMBER_LITERAL = "([0-9]+)";
    public static final String STRING_LITERAL = "(\\\".*?\\\")";
    public static final String ID_LITERAL     = "([A-Za-z][A-Za-z0-9]*)";
    public static final String OP             = "(>|<|=+|<=|>=|&&|\\|\\||\\+|\\-|\\*|/)";
    public static final String BRACE          = "(\\(|\\)|\\{|\\})";
    public static final String OTHER          = "(\\p{Punct})";

    public static final String TOKEN_PAT = SPACE + "(" + COMMENT + "|" + NUMBER_LITERAL + "|" + STRING_LITERAL + "|" + ID_LITERAL + "|" + OP + "|" + BRACE + "|" + OTHER + ")";

    private final Pattern          pattern    = Pattern.compile(TOKEN_PAT);
    private final List<Token>      tokenQueue = new ArrayList<>();
    private final LineNumberReader lineNumberReader;
    private       boolean          hasMore    = true;

    public Lexer(LineNumberReader lineNumberReader) {
        this.lineNumberReader = lineNumberReader;
    }

    public Token read() throws IOException {
        if (fillQueue()) {
            return tokenQueue.remove(0);
        } else {
            return Token.EOF;
        }
    }

    private boolean fillQueue() throws IOException {
        while (0 >= tokenQueue.size()) {
            if (hasMore) {
                readLine();
            } else {
                return false;
            }
        }
        return true;
    }

    private void readLine() throws IOException {
        String line = lineNumberReader.readLine();
        if (line == null) {
            hasMore = false;
            return;
        }

        int     lineNo  = lineNumberReader.getLineNumber();
        Matcher matcher = pattern.matcher(line);
        matcher.useTransparentBounds(true).useAnchoringBounds(false);
        int pos    = 0;
        int endPos = line.length();
        while (pos < endPos) {
            matcher.region(pos, endPos);
            if (matcher.lookingAt()) {
                addToken(lineNo, matcher);
                pos = matcher.end();
            } else {
                throw new RuntimeException("词法分析器异常");
            }
        }
        tokenQueue.add(new OtherToken(lineNo, Token.EOL));
    }

    private void addToken(int lineNo, Matcher matcher) {
        String m = matcher.group(1);
        if (m != null) {
            Token token;
            if (matcher.group(2) != null) {
                // 注释
                token = new CommentToken(lineNo, m);
            } else if (matcher.group(3) != null) {
                // 数字
                token = new NumToken(lineNo, Integer.valueOf(m));
            } else if (matcher.group(4) != null) {
                // 字符串
                token = new StringToken(lineNo, m);
            } else if (matcher.group(5) != null) {
                // id
                token = new IdToken(lineNo, m);
            } else if (matcher.group(6) != null) {
                // 操作符
                token = new OperatorToken(lineNo, m);
            } else if (matcher.group(7) != null) {
                // 括号
                token = new BraceToken(lineNo, m);
            } else if (matcher.group(8) != null) {
                // 其他
                token = new OtherToken(lineNo, m);
            } else {
                token = new UnknownToken(lineNo, m);
            }
            tokenQueue.add(token);
        }
    }

    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader("/Users/yehao/Workspace2/Java/mylang/src/main/java/TestSource.txt");
        Lexer      lexer      = new Lexer(new LineNumberReader(fileReader));
        Token      token;
        while ((token = lexer.read()) != Token.EOF) {
            if (!(token instanceof OtherToken)) {
                System.out.println(token);
            }
        }
    }
}
