
public class Foo {
    public ModelAndView viewEntry(Model model, HttpServletRequest request) {
        //call private method.  Flagged Method
        return view(model, VIEW);
    }

    /** THIS IS FLAGGED AS UNUSED **/
    private ModelAndView view(Model model, String view) {
        //add values to the model

        //return the correct view
        return new ModelAndView(view, MVC_CONSTANTS.MODEL_KEY, model.asMap());
    }
}
        