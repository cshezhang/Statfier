import groovy.lang.Closure;
import java.io.Serializable;
import java.lang.reflect.Method;
import org.codehaus.groovy.runtime.ConversionHandler;

public class ConvertedClosure extends ConversionHandler implements Serializable {

  private String methodName;
  private static final long serialVersionUID = 1162833713450835227L;

  public ConvertedClosure(Closure closure, String method) {
    super(closure);
    this.methodName = method;
  }

  public ConvertedClosure(Closure closure) {
    this(closure, (String) null);
  }

  public Object invokeCustom(Object proxy, Method method, Object[] args) throws Throwable {
    return this.methodName != null && !this.methodName.equals(method.getName())
        ? null
        : ((Closure) this.getDelegate()).call(args);
  }
}