
public class AccountSelectionSubForm extends Form implements WidgetEventListener
{
    public static class Factory
    {
        public Factory()
        {
            // do
        }
        // factory method which creates the outer class
        public AccountSelectionSubForm create( Form parent, boolean supportAllAccountsSelection )
        {
            return new AccountSelectionSubForm();
        }
    }

    private AccountSelectionSubForm()
    {
        super( parent, null );
    }
}
        