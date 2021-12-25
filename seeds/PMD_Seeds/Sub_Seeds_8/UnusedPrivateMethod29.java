
public class Foo {
    public ModelAndView showLineGraph(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //call private method  USAGE #1.  Method not flagged
        MyObject filter = getGraphInnateFilter(request);

        //LINE GRAPHIC LOGIC

        //write output to response stream and return
    }

    public ModelAndView showPieChart(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //call private method  USAGE #2.  Method not flagg
        MyObject filter = getGraphInnateFilter(request);

        //PIE CHART LOGIC

        //write output to response stream and return
    }

    /** This method is NOT flagged as unused **/
    private MyObject getGraphInnateFilter(HttpServletRequest request) {
        MyObject filter = new MyObject();

        //call private method.  Flagged method
        setInnateFilterFields(filter, request);
        //perform logic

        //return
        return filter;
    }

    /** THIS IS FLAGGED AS UNUSED **/
    private void setInnateFilterFields(MyObject filter, HttpServletRequest request) {
        //add values to filter object
    }
}
        