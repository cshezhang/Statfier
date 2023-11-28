public class RequiredPropsSection {

  public MySection mMySection;

  public Section buildWithAllOk() {
    return mMySection.create().prop1(new Object()).prop2(new Object()).build();
  }

  // prop 2 is optional
  public Section buildWithout2Ok() {
    return mMySection.create().prop1(new Object()).build();
  }

  // prop 1 is required
  public Section buildWithout1Bad() {
    return mMySection.create().prop2(new Object()).build();
  }
}

