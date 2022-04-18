import java.util.Random;

public class Case3 {

    public void testRandomCast() {
        Random random = new Random();
        int a = (int)Math.random();
        a = (int)random.nextFloat();
        a = (int)random.nextDouble();
    }

}
