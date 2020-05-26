package ast;

public class Context {

    private static ThreadLocal<Environment> environmentThreadLocal = new ThreadLocal<>();

    static {
        environmentThreadLocal.set(new Environment());
    }

    public static Environment get() {
        return environmentThreadLocal.get();
    }
}
