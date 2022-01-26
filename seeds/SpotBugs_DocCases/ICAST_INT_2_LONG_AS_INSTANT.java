public class ICAST_INT_2_LONG_AS_INSTANT {
    
    // Fails for dates after 2037
    Date getDate(int seconds) {
        return new Date(seconds * 1000L);
    }

    // better, works for all dates
    Date getDate(long seconds) {
        return new Date(seconds * 1000);
    }

}