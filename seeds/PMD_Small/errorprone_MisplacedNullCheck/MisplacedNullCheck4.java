
public class Foo {
    public final void setExcludeStatus(String[] excludeStatus) {
        if (excludeStatus != null) {
            this.excludeStatus = Arrays.asList(excludeStatus.clone());
        } else {
            this.excludeStatus = null;
        }
    }
}
        