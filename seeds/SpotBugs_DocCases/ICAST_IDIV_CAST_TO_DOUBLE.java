public class ICAST_IDIV_CAST_TO_DOUBLE {
    int x = 2;
    int y = 5;
    // Wrong: yields result 0.0
    double value1 = x / y;

    // Right: yields result 0.4
    double value2 = x / (double) y;
}