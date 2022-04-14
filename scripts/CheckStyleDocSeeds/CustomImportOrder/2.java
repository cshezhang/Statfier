

package com.company;

import static java.util.*; // OK
import static java.io.*; // OK

import java.time.*; // OK
import javax.net.*; // violation as it is not included in standard java package group.

import org.apache.commons.io.FileUtils; // violation
import com.puppycrawl.tools.checkstyle.checks.imports.CustomImportOrderCheck; // OK
import com.puppycrawl.tools.checkstyle.checks.imports.ImportOrderCheck; // OK
        