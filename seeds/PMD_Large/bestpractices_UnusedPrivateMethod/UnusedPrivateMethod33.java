
import net.sourceforge.pmd.lang.java.rule.bestpractices.unusedprivatemethod.*;

public class Foo extends MultiActionController {
    private static final Logger logger = Logger.getLogger(Foo.class);

    public DashboardGraphInnateFilter getGraphInnateFilter(HttpServletRequest request) {
        DashboardGraphInnateFilter filter = new DashboardGraphInnateFilter();
        setInnateFilterFields(filter, request);
        return filter;
    }

    private void setInnateFilterFields(DashboardInnateFilter filter, HttpServletRequest request) { //incorrectly flagged
        logger.info("here");
    }
}
        