
public abstract class Animal {  // Noncompliant; should be an interface
  abstract void move();
  abstract void feed();
}

public abstract class Color {  // Noncompliant; should be concrete with a private constructor
  private int red = 0;
  private int green = 0;
  private int blue = 0;

  public int getRed() {
    return red;
  }
}
