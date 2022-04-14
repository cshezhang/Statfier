package edu.polyu;

import net.sourceforge.pmd.PMD;

import java.io.File;

/**
 * Description:
 * Author: Vanguard
 * Date: 2022/2/24 7:54 PM
 */
public class UnitTester {

    public static void main(String[] args) {
        String[] pmdConfig = {
                "-d", "./seeds/SingleTesting" + File.separator + "bestpractices_MissingOverride",
                "-R", "category/java/" + "bestpractices" + ".xml/MissingOverride",
                "-f", "html",
                "-r", "./result.html"
//                "--no-cache"
        };
        PMD.main(pmdConfig);
    }


}
