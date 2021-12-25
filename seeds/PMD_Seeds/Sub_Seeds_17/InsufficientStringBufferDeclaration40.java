
public class Test {
    public void testStringBufferWithHexInt() {
        // 0xdeadbeef == 3735928559.length() = 10

        StringBuffer myVar = new StringBuffer();
        myVar.append(0xdeadbeef);
        myVar.append(0xdeadbeef);

        StringBuilder myVar2 = new StringBuilder();
        myVar2.append(0xdeadbeef);
        myVar2.append(0xdeadbeef);
    }
}
        