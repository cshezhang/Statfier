package net.sourceforge.pmd.lang.java.rule.bestpractices.missingoverride;


public class ConcreteClassArrayParams extends AbstractClass {

  // missing
  public void arrayParams(String dflt, int[] keys, StringBuilder[] labels) {
    super.arrayParams(dflt, keys, labels);
  }
}

