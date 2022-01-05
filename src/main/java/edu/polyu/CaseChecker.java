package edu.polyu;

import java.io.FileReader;
import java.io.Reader;

import static edu.polyu.Util.sep;

public class CaseChecker {

    public static void readCSVFile() {
        try {
            String[] HEADERS = { "author", "title"};
            Reader in = new FileReader("." + sep + "scripts" + sep + "PMDSeedData.csv");
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {

    }


}
