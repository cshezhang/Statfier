/*
CustomImportOrder
customImportOrderRules = THIRD_PARTY_PACKAGE###SAME_PACKAGE(6)###STANDARD_JAVA_PACKAGE###\
                         SPECIAL_IMPORTS
standardPackageRegExp = com.puppycrawl.tools.*Check$
thirdPartyPackageRegExp = com.puppycrawl.tools.checkstyle.checks.javadoc.*Javadoc*
specialImportsRegExp = com.puppycrawl.tools.*Tag*
separateLineBetweenGroups = (default)true
sortImportsInGroupAlphabetically = (default)false


*/

// non-compiled with javac: special package and requires imports from the same package
package com.puppycrawl.tools.checkstyle.checks.imports.customimportorder;


// every import from javadoc package has comment in brackets indicating presence of keywords
// Javadoc, Check, Tag. For example J_T = Javadoc, no Check, Tag
// violation

// STANDARD - keyword Check

// violation
// (_C_)
// (JCT)

// SPECIAL_IMPORTS - keyword Tag

// violation
// import com.puppycrawl.tools.checkstyle.checks.javadoc.TagParser; // (__T)
// violation

import com.puppycrawl.tools.checkstyle.*;
// import com.puppycrawl.tools.checkstyle.checks.javadoc.HtmlTag;
// violation
// violation
// violation

public class InputCustomImportOrder_OverlappingPatterns {}

