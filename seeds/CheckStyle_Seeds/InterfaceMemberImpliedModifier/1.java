public interface RoadFeature {

  String STOP = "Stop"; // violation

  enum Lights { // valid because of configured properties
    RED,
    YELLOW,
    GREEN;
  }
}

