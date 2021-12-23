
public class NotificationPacket {
    public NotificationPacket(byte id, byte[] rawTypeData)
    {
        super(id);
        if (rawTypeData == null)
        {
            throw new IllegalArgumentException("No type data is specified");
        }
        if (rawTypeData.length == 0)
        {
            this.message = EMPTY_MESSAGE;
        }
        else
        {
            this.message = new String(rawTypeData, StandardCharsets.UTF_8);
        }
    }
}
        