



import codetoanalyze.java.annotation.ThreadSafe;

@ThreadSafe
class DoNotReport {

  int mFld;

  // normally we would report this, but we won't because com.racerd.donotreport is block listed in
  // .inferconfig
  void obviousRaceBad(int i) {
    mFld = i;
  }
}
