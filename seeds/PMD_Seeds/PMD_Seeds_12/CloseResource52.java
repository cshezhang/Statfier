
import java.util.stream.*;

public class CloseResourceStreamInt {
     public void reproduceIntStream() {
        IntStream iStream = IntStream.of(1).filter(i -> i < 5);
        iStream.anyMatch(i -> i < 5);
    }

    public void reproduceIntStream() {
        LongStream lStream = LongStream.of(1).filter(i -> i < 5);
        lStream.anyMatch(i -> i < 5);
    }

    public void reproduceIntStream() {
        DoubleStream dStream = DoubleStream.of(1).filter(i -> i < 5);
        dStream.anyMatch(i -> i < 5);
    }
}
        