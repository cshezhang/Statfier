

package com.company;

import static java.io.*; // OK
import static java.util.*; // OK
import java.time.*; // violation
import javax.net.*; // violation

import com.puppycrawl.tools.checkstyle.checks.imports.CustomImportOrderCheck; // OK
import com.puppycrawl.tools.checkstyle.checks.imports.ImportOrderCheck; // OK
import org.apache.commons.io.FileUtils; // OK
        