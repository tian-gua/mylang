package parser;

import ast.*;
import exception.EmptyException;
import exception.ParserException;
import lexer.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class Parser {
    private List<AstTree> astTreeList = new ArrayList<>();

    private final Stack<Token> tokenStack;

    public Parser(List<Token> tokenList) {
        this.tokenStack = new Stack<>();
        Stack<Token> tempStack = new Stack<>();
        for (Token token : tokenList) {
            if (token instanceof CommentToken) {
                continue;
            }
            tempStack.push(token);
        }
        while (!tempStack.empty()) {
            tokenStack.push(tempStack.pop());
        }
    }

    public List<AstTree> parse() {
        while (true) {
            try {
                while (isEOL()) {
                    drop();
                    if (tokenStack.isEmpty()) {
                        return astTreeList;
                    }
                }
                if (check(IdToken.class, "if")) {
                    astTreeList.add(ifTree());
                }
                if (check(IdToken.class, "while")) {
                    astTreeList.add(whileTree());
                } else if (check(IdToken.class) && check(1, OperatorToken.class, "=")) {
                    astTreeList.add(assign(true));
                } else if (check(IdToken.class) && isEOL(1)) {
                    Token  token = eat();
                    Object value = new IdLeaf(token).eval();
                    if (value instanceof String) {
                        back(new StringToken(token.getLine(), "\"" + value + "\""));
                    }
                } else if (check(IdToken.class, "print")) {
                    Token cmd = eat();
                    astTreeList.add(new CmdTree((String) cmd.value(), exp(true)));
                } else {
                    astTreeList.add(exp(true));
                }
            } catch (EmptyException e) {
                return astTreeList;
            }
        }
    }

    public boolean check(Class<? extends Token> tokenClass) {
        return check(0, tokenClass);
    }

    public boolean check(Class<? extends Token> tokenClass, String value) {
        return check(0, tokenClass, value);
    }

    public boolean check(int position, Class<? extends Token> tokenClass) {
        return check(position, tokenClass, null);
    }

    public boolean check(int position, Class<? extends Token> tokenClass, String value) {
        Token token = tokenStack.get(tokenStack.size() - 1 - position);
        if (value == null) {
            return tokenClass.isInstance(token);
        }
        return tokenClass.isInstance(token) && token.value().equals(value);
    }

    public boolean isEOL() {
        return tokenStack.isEmpty() || tokenStack.get(tokenStack.size() - 1).value().equals(Token.EOL);
    }

    public boolean isEOL(int position) {
        return tokenStack.isEmpty() || tokenStack.get(tokenStack.size() - 1 - position).value().equals(Token.EOL);
    }

    /**
     * assign
     *
     * @author yehao
     * @date 2020/5/25
     */
    public AssignTree assign(boolean preEval) {
        IdLeaf  idLeaf = new IdLeaf(eat());
        Token   op     = eat();
        ExpTree exp    = exp(preEval);
        return new AssignTree(idLeaf, new AstLeaf(op), exp);
    }

    /**
     * exp
     *
     * @author yehao
     * @date 2020/5/22
     */
    public ExpTree exp(boolean preEval) {
        if (preEval) {
            boolean needPreEval = false;
            int     i           = 0;
            while (!isEOL(i)) {
                if (check(i, IdToken.class)) {
                    needPreEval = true;
                    break;
                }
                i++;
            }
            if (needPreEval) {
                Stack<Token> tempStack = new Stack<>();
                i = 0;
                while (!isEOL(i)) {
                    Token token = eat();
                    if (token instanceof IdToken) {
                        Object value = new IdLeaf(token).eval();
                        if (Objects.isNull(value)) {
                            throw new NullPointerException(((IdToken) token).value() + "为空");
                        }
                        if (value instanceof String) {
                            tempStack.push(new StringToken(token.getLine(), "\"" + value + "\""));
                        } else if (value instanceof Integer) {
                            tempStack.push(new NumToken(token.getLine(), (Integer) value));
                        } else if (value instanceof Boolean) {
                            tempStack.push(new BoolToken(token.getLine(), (Boolean) value));
                        }
                    } else {
                        tempStack.push(token);
                    }
                }
                while (!tempStack.isEmpty()) {
                    back(tempStack.pop());
                }
            }
        }


        if (check(StringToken.class) || (check(IdToken.class) && currentId() instanceof String)) {
            List<AstTree> tempAstTreeList = new ArrayList<>();
            while (!isEOL()) {
                Token token = eat();
                if (token instanceof StringToken) {
                    tempAstTreeList.add(new StringLeaf(token));
                } else if (token instanceof NumToken) {
                    tempAstTreeList.add(new NumLeaf(token));
                } else if (token instanceof IdToken) {
                    tempAstTreeList.add(new IdLeaf(token));
                } else {
                    tempAstTreeList.add(new AstLeaf(token));
                }
            }
            return new ExpTree(tempAstTreeList);
        } else if (check(BoolToken.class)) {
            return new ExpTree(new AstLeaf(eat()));
        } else {
            TermTree termTree = term();
            if (check(OperatorToken.class)) {
                Token   op      = eat();
                ExpTree expTree = exp(true);
                return new ExpTree(termTree, new AstLeaf(op), expTree);
            }
            return new ExpTree(termTree);
        }
    }

    /**
     * number
     *
     * @author yehao
     * @date 2020/5/22
     */
    public NumLeaf num() {
        if (check(NumToken.class)) {
            return new NumLeaf(eat());
        } else {
            throw new ParserException(tokenStack.get(0), "NumToken");
        }
    }

    /**
     * factor
     *
     * @author yehao
     * @date 2020/5/22
     */
    public FactorTree factor() {
        if (check(BraceToken.class, "(")) {
            Token   leftBrace  = eat();
            ExpTree expTree    = exp(true);
            Token   rightBrace = eat();
            return new FactorTree(new AstLeaf(leftBrace), expTree, new AstLeaf(rightBrace));
        } else {
            if (check(NumToken.class)) {
                return new FactorTree(num());
            } else if (check(StringToken.class)) {
                return new FactorTree(new StringLeaf(eat()));
            } else {
                return new FactorTree(new IdLeaf(eat()));
            }
        }
    }


    /**
     * term
     *
     * @author yehao
     * @date 2020/5/22
     */
    public TermTree term() {
        FactorTree left = factor();
        if (check(OperatorToken.class, "*") || check(OperatorToken.class, "/")) {
            AstLeaf    op           = new AstLeaf(eat());
            FactorTree right        = factor();
            TermTree   tempTermTree = new TermTree(left, op, right);
            while (check(OperatorToken.class, "*") || check(OperatorToken.class, "/")) {
                AstLeaf    tempOp    = new AstLeaf(eat());
                FactorTree tempRight = factor();
                tempTermTree = new TermTree(tempTermTree, tempOp, tempRight);
            }
            return tempTermTree;
        } else {
            return new TermTree(left);
        }
    }

    /**
     * if
     *
     * @author yehao
     * @date 2020/5/25
     */
    public IFTree ifTree() {
        eat();//if
        eat();//(
        ExpTree expTree = exp(true);
        eat();//(
        Boolean condition = (Boolean) expTree.eval();
        if (!condition) {
            eat();//{
            while (!check(BraceToken.class, "}")) {
                drop();
            }
            eat();//}
            return new IFTree(expTree, null);
        } else {
            BlockTree blockTree = block();
            return new IFTree(expTree, blockTree);
        }
    }

    /**
     * while
     *
     * @author yehao
     * @date 2020/5/25
     */
    public WhileTree whileTree() {
        eat();//if
        eat();//(
        ExpTree expTree = exp(false);
        eat();//(
        Boolean condition = (Boolean) expTree.eval();
        if (!condition) {
            eat();//{
            while (!check(BraceToken.class, "}")) {
                drop();
            }
            eat();//}
            return new WhileTree(expTree, null);
        } else {
            BlockTree blockTree = block();
            return new WhileTree(expTree, blockTree);
        }
    }

    /**
     * block
     *
     * @author yehao
     * @date 2020/5/25
     */
    public BlockTree block() {
        List<AstTree> astTreeList = new ArrayList<>();
        BlockTree     blockTree   = new BlockTree(astTreeList);
        eat();//{
        while (true) {
            while (isEOL()) {
                drop();
                if (check(BraceToken.class, "}")) {
                    eat();
                    return blockTree;
                }
            }
            if (check(IdToken.class) && check(1, OperatorToken.class, "=")) {
                astTreeList.add(assign(false));
            } else if (check(IdToken.class) && isEOL(1)) {
                Token  token = eat();
                Object value = new IdLeaf(token).eval();
                if (value instanceof String) {
                    back(new StringToken(token.getLine(), "\"" + value + "\""));
                }
            } else if (check(IdToken.class, "print")) {
                Token cmd = eat();
                astTreeList.add(new CmdTree((String) cmd.value(), exp(false)));
            } else {
                astTreeList.add(exp(false));
            }
        }
    }

    private Token eat() {
        return tokenStack.pop();
    }

    private void drop() {
        tokenStack.pop();
    }

    public void back(Token token) {
        tokenStack.push(token);
    }

    public Object currentId() {
        return Context.get().getId((String) tokenStack.get(tokenStack.size() - 1).value());
    }

    public static void main(String[] args) throws IOException {
        FileReader  fileReader = new FileReader("/Users/yehao/Workspace2/Java/mylang/src/main/java/TestSource.ylang");
        Lexer       lexer      = new Lexer(new LineNumberReader(fileReader));
        Token       token;
        List<Token> tokenList  = new ArrayList<>();
        while ((token = lexer.read()) != Token.EOF) {
            tokenList.add(token);
        }
        Parser        parser      = new Parser(tokenList);
        List<AstTree> astTreeList = parser.parse();
//        for (AstTree astTree : astTreeList) {
//            astTree.eval();
//        }
    }
}
