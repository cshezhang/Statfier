
import java.util.Map;

public class TestPrivate {
    public <T extends Map<?,?>, X extends Map<?,?>>  void createLedgerAndChangeHistory(T oldObject, X updatedObject, boolean isChildLedgerEntry) throws ServiceException {
        Map.Entry entry = new Map.Entry();

        try {
            if (oldObject instanceof BudgetPackage || oldObject instanceof SpiEntity) {
                setTotals(oldObject);
            }
        // do other stuff
        } catch (ServiceException e) {
        }
    }

    private <X extends Map> void setTotals(X ledgerable) throws ServiceException {
        // do stuff
    }
}
        