
public class Bar {
    private int x = 0b0101;
    private int y = 0B0101;
    private long xl = 0b0101l;
    private long yl = 0B0101L;

    private int a = 12_34;
    private int b = 0xff_00_00_00;
    private long c = 0b11010010_01101001_10010100_10010010;

    // now problem cases
    private int iszero1 = 0b0;
    private int iszero2 = 0B0;
    private long iszero3 = 0b0l;
    private long iszero4 = 0B0L;
    private int iszero5 = 0b0_______0;
}
        