import net.jcip.annotations.Immutable;

@Immutable
public class Test {
    final Object anonWrap125 =
            new Object() {
                final int x = 0;
            };
    int y;
}