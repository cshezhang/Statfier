
public class Foo extends MultiActionController {

    private static final Logger logger = Logger.getLogger(Foo.class);

    public DashboardGraphInnateFilter getGraphInnateFilter(HttpServletRequest request) {
        setInnateFilterFields();
        return null;
    }

    private void setInnateFilterFields() { //Not flagged
        logger.info("here");
    }
}
        