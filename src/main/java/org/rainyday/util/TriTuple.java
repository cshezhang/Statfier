package org.rainyday.util;

public class TriTuple {
    public String first;
    public String second;
    public String third;

    // third String means FP or FN
    public TriTuple(String first, String second, String third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TriTuple) {
            TriTuple rhs = (TriTuple) o;
            if (rhs.first.equals(this.first) && rhs.second.equals(this.second) && rhs.third.equals(this.third)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.first + this.second + this.third).hashCode();
    }

    @Override
    public String toString() {
        return "Seed: " + first + "\nMutant: " + second + "\nBug Type: " + third;
    }

}
