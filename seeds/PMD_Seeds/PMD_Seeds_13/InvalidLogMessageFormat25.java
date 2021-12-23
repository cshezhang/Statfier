
package my.test;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Test {

    private static final Logger log = LogManager.getLogger(Test.class);

    public static void main(String[] args) {
        log.info("1" + "2");
    }
}
        