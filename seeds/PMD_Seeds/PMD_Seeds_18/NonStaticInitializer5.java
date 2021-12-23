
public class Foo {
    public Animation getStatusTransition() {
        return new Transition() {

            {
                setCycleDuration(Duration.millis(1200));
                class ImInAnon {{}} // should be flagged
            }

            @Override
            protected void interpolate(double frac) {
                // magic
            }
        };
     }
}
        