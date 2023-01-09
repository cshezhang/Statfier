

package com.company;

import static java.io.*; // OK
import static java.util.*; // OK

import java.time.*; // violation should be in standard package group
                   // below special import

import javax.net.*; // Violation should be in special import group

import org.apache.commons.io.FileUtils; // Violation should be in
                                       // THIRD PARTY PACKAGE GROUP
import com.puppycrawl.tools.checkstyle.checks.imports.CustomImportOrderCheck; // Violation
import com.puppycrawl.tools.checkstyle.checks.imports.ImportOrderCheck; // Violation
        