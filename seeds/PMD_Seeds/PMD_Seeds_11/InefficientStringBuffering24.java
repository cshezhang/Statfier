
public class Test {
    public void method() {
        int initialSizeGlobal = (int) (profilingContext.m_profileItems.size() * (150.0 * 0.30));
        int initialSizeDetail = s_profiling_details_enabled ? profilingContext.m_overallProfileItems.size() * 150 : 0;
        StringBuilder sb = new StringBuilder(initialSizeGlobal + initialSizeDetail);
    }
}
        