
public class Bug2317099
{
    public void method(Env env) {
        if (env != null && env.getContext() != null &&
            env.getContext().getContextObject() instanceof PageContext) {
        }
    }
}
        