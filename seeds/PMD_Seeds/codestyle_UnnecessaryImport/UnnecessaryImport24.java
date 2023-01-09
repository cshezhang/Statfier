
import a.b.c.d.AgentState;
import a.b.c.d.OtherState;

public interface Foo {
    /**
     * Gets all active AgentHandles.
     * <p/>
     * An agent is active if it has not posted a {@link AgentStateChangeEvent} containing {@link AgentState#TERMINATED}.
     *
     * @return agent handles.
     * @see OtherState#TERMINATED
     */
    Iterable<AgentHandle> getAgentHandles();
}
        