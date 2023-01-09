
public class Foo {
    public Animation getStatusTransition() {
        return new Transition() {

            {
                setCycleDuration(Duration.millis(1200));
            }

            @Override
            protected void interpolate(double frac) {
                // magic
            }
        };
    }
}
        