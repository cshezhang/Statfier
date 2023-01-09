
public enum Operation
{
    LEFT( Action.PRIVATE_MOVE ), RIGHT( Action.PRIVATE_MOVE ), STOP( Action.PRIVATE_STAY );

    private final int action;

    private Operation(final int action)
    {
        this.action = action;
    }

    public int getAction()
    {
        return action;
    }

    private static final class Action
    {
        private static final int PRIVATE_STAY = 0;
        private static final int PRIVATE_MOVE = 1;
    }
}
        