
public class WishDTO {
  Long productId;
  Long quantity;
  Long clientId;
}

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PurchaseOrderController {

  @PostMapping(path = "/saveForLater")
  public String saveForLater(WishDTO wish) {
    Wish persistentWish = new Wish();
    // do the mapping between "wish" and "persistentWish"
    [...]
    session.save(persistentWish);
  }

  @RequestMapping(path = "/saveForLater", method = RequestMethod.POST)
  public String saveForLater(WishDTO wish) {
    Wish persistentWish = new Wish();
    // do the mapping between "wish" and "persistentWish"
    [...]
    session.save(persistentWish);
  }
}
