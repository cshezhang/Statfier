/*
ImportOrder
option = (default)under
groups = (default)
ordered = (default)true
separated = (default)false
separatedStaticGroups = (default)false
caseSensitive = (default)true
staticGroups = (default)
sortStaticImportsAlphabetically = (default)false
useContainerOrderingForStatic = (default)false
tokens = (default)STATIC_IMPORT


*/

package com.puppycrawl.tools.checkstyle.checks.imports.importorder;

// ok
// ok
// violation
// ok
// ok
// ok
// ok
import static javax.swing.WindowConstants.*; // ok
// violation

// violation
// violation
// ok
// ok
// ok

public class InputImportOrder_Under {}

