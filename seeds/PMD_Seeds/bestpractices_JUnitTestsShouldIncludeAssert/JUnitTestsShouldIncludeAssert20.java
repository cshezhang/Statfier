import javafx.embed.swing.JFXPanel;
import org.junit.*;

public class BaseConsoleTest extends UART {
  @Test
  public void testInitialize() throws InterruptedException {
    new JFXPanel(); // AllocationExpression
  }
}

