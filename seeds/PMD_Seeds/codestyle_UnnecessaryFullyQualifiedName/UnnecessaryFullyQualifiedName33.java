
public class JavaLang {
    public void foo() {
        ProcessBuilder pb = new ProcessBuilder("echo");
        pb.redirectError(java.lang.ProcessBuilder.Redirect.DISCARD); // violation
    }
}
        