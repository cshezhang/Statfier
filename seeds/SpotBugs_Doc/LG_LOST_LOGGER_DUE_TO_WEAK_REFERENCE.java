public class LG_LOST_LOGGER_DUE_TO_WEAK_REFERENCE {
  public static void initLogging() throws Exception {
    Logger logger = Logger.getLogger("edu.umd.cs");
    logger.addHandler(new FileHandler()); // call to change logger configuration
    logger.setUseParentHandlers(false); // another call to change logger configuration
  }

  public static void main(String[] args) throws Exception {
    initLogging(); // adds a file handler to the logger
    System.gc(); // logger configuration lost
    Logger.getLogger("edu.umd.cs")
        .info("Some message"); // this isn't logged to the file as expected
  }
}

