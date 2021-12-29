
public final class Test {
    public String foo() {
        final StringBuilder sb = new StringBuilder();
        sb.append("literal1");
        try {
            final String res = bar();
            sb.append(res);
            sb.append("literal2");
        } catch (ArithmeticException e) {
            sb.append("literal3");
        } catch (ArrayIndexOutOfBoundException e) {
            sb.append("literal4");
        } catch (Exception e) {
            sb.append("literal5");
        } finally {
            sb.append("literal6");
        }
        return sb.toString();
    }

    public String bar() throws Exception {
        throw new Exception();
    }
}
        