import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StrutsV1Action extends Action {

  private static final Logger logger = Logger.getLogger(StrutsV1Action.class.getName());

  @Override
  public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response)
      throws Exception {

    TestForm testForm = (TestForm) form;

    logger.fine("This is a Struts endpoint!" + testForm.getItemId());

    return mapping.findForward("SUCCESS");
  }
}

class TestForm extends ActionForm {

  private String itemId;
  private String price;

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }
}
