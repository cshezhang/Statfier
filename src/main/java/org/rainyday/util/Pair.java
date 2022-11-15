package org.rainyday.util;

/**
 * @Description: Pair Class
 * @Author: Vanguard
 * @Date: 2021-12-29 19:14
 */
public class Pair {
    public String first;
    public String second;

    public Pair(String first, String second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Pair) {
            Pair rhs = (Pair) o;
            if(rhs.first.equals(this.first) && rhs.second.equals(this.second)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.first + this.second).hashCode();
    }

}