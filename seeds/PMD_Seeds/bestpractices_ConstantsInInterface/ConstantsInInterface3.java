
package some.pkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface SomeInterfaceName {

    /**
     * The logger service managers can use to record traces.
     */
    Logger LOGGER = LoggerFactory.getLogger(SomeInterfaceName.class);
}
        