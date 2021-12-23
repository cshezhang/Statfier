
import org.junit.*;
import javafx.embed.swing.JFXPanel;

public class BaseConsoleTest extends UART {
    @Test
    public void testInitialize() throws InterruptedException {
        new JFXPanel(); // AllocationExpression
    }
}
        