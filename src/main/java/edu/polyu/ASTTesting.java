package edu.polyu;

import edu.polyu.analysis.TypeWrapper;

import java.util.ArrayList;

public class ASTTesting {


    public static void foo(ArrayList<Integer> arr) {
        arr.add(3);
    }


    public static void main(String[] args) {
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(1);
        arr.add(2);
        foo(arr);
        System.out.println(arr.size());
    }

}
