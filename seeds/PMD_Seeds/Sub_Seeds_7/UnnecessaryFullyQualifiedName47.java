
package com.example;

public class ClassOne extends com.example.ClassTwo {
    com.example.ClassTwo.Builder builder = null;

    public void run() throws com.example.ClassOne.MyException {
        com.example.ClassOne.Builder builder = null;
        com.example.ClassTwo.Builder builder2 = null;
        throw new com.example.ClassOne.MyException();
    }

    public class Builder extends com.example.ClassTwo.Builder {
    }

    public static class MyException extends Exception { }
}
        