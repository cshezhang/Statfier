

package com.company;

import static java.io.*; // OK
import static java.util.*; // OK

import java.time.*; // OK
import javax.net.*; // OK

import com.puppycrawl.tools.checkstyle.checks.imports.CustomImportOrderCheck; // Violation
import com.puppycrawl.tools.checkstyle.checks.imports.ImportOrderCheck; // Violation

import org.apache.commons.io.FileUtils;
        