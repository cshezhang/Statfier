
public class CastToShort {
    public void testcase() {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort((short) "1111".getBytes().length);
    }
}
        