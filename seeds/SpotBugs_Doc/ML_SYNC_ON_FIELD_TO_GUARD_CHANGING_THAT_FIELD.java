public class ML_SYNC_ON_FIELD_TO_GUARD_CHANGING_THAT_FIELD {
    private Long myNtfSeqNbrCounter = new Long(0);
    private Long getNotificationSequenceNumber() {
        Long result = null;
        synchronized(myNtfSeqNbrCounter) {
            result = new Long(myNtfSeqNbrCounter.longValue() + 1);
            myNtfSeqNbrCounter = new Long(result.longValue());
        }
        return result;
    }
}