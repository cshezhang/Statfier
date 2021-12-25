
public class Foo {
    String VIEW = "foo";

    @RequestMapping(method = RequestMethod.GET, value = URL_MAPPINGS.BULK_ACTION_URL)
    public ModelAndView viewEntry(Model model, HttpServletRequest request, @RequestParam(value = "fiscalYear", required = true) int fy) {
        return view(model, VIEW, fy);
    }

    private ModelAndView view(Model model, String view, int fy) {
        //add values to the model
        model.addAttribute(FIELDS.FISCAL_YEAR, fy);

        //return the correct view
        return new ModelAndView(view, MVC_CONSTANTS.MODEL_KEY, model.asMap());
    }
}
        