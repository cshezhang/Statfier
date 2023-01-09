

package com.company;

import static java.util.*; // OK

import java.time.*; // OK
import javax.net.*; // OK
import static java.io.*; // violation as static imports should be in top

import org.apache.commons.io.FileUtils; // OK
import com.puppycrawl.tools.checkstyle.checks.imports.CustomImportOrderCheck; // OK
import com.puppycrawl.tools.checkstyle.checks.imports.ImportOrderCheck; // OK
        