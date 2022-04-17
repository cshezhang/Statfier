////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2021 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////

package com.puppycrawl.tools.checkstyle.checks.whitespace;

import static com.puppycrawl.tools.checkstyle.checks.whitespace.EmptyLineSeparatorCheck.MSG_MULTIPLE_LINES;
import static com.puppycrawl.tools.checkstyle.checks.whitespace.EmptyLineSeparatorCheck.MSG_MULTIPLE_LINES_AFTER;
import static com.puppycrawl.tools.checkstyle.checks.whitespace.EmptyLineSeparatorCheck.MSG_MULTIPLE_LINES_INSIDE;
import static com.puppycrawl.tools.checkstyle.checks.whitespace.EmptyLineSeparatorCheck.MSG_SHOULD_BE_SEPARATED;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.AbstractModuleTestSupport;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

public class EmptyLineSeparatorCheckTest
    extends AbstractModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/puppycrawl/tools/checkstyle/checks/whitespace/emptylineseparator";
    }

    @Test
    public void testGetRequiredTokens() {
        final EmptyLineSeparatorCheck checkObj = new EmptyLineSeparatorCheck();
        assertArrayEquals(CommonUtil.EMPTY_INT_ARRAY, checkObj.getRequiredTokens(),
                "EmptyLineSeparatorCheck#getRequiredTokens should return empty array by default");
    }

    @Test
    public void testDefault() throws Exception {

        final String[] expected = {
            "33:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "import"),
            "50:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "CLASS_DEF"),
            "53:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "VARIABLE_DEF"),
            "54:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "STATIC_INIT"),
            "58:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "INSTANCE_INIT"),
            "72:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "CTOR_DEF"),
            "77:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "METHOD_DEF"),
            "94:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "METHOD_DEF"),
            "125:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "INTERFACE_DEF"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparator.java"), expected);
    }

    @Test
    public void testAllowNoEmptyLineBetweenFields() throws Exception {

        final String[] expected = {
            "33:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "import"),
            "50:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "CLASS_DEF"),
            "54:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "STATIC_INIT"),
            "58:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "INSTANCE_INIT"),
            "72:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "CTOR_DEF"),
            "77:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "METHOD_DEF"),
            "94:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "METHOD_DEF"),
            "125:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "INTERFACE_DEF"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparator2.java"), expected);
    }

    @Test
    public void testHeader() throws Exception {
        final String[] expected = {
            "31:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "package"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorHeader.java"), expected);
    }

    @Test
    public void testMultipleEmptyLinesBetweenClassMembers() throws Exception {
        final String[] expected = {
            "33:1: " + getCheckMessage(MSG_MULTIPLE_LINES, "package"),
            "36:1: " + getCheckMessage(MSG_MULTIPLE_LINES, "import"),
            "41:1: " + getCheckMessage(MSG_MULTIPLE_LINES, "CLASS_DEF"),
            "45:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "VARIABLE_DEF"),
            "50:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "VARIABLE_DEF"),
            "55:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "METHOD_DEF"),
            "57:33: " + getCheckMessage(MSG_MULTIPLE_LINES_AFTER, "}"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorMultipleEmptyLines.java"), expected);
    }

    @Test
    public void testFormerArrayIndexOutOfBounds() throws Exception {
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorFormerException.java"), expected);
    }

    @Test
    public void testAllowMultipleFieldInClass() throws Exception {
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorMultipleFieldsInClass.java"), expected);
    }

    @Test
    public void testAllowMultipleImportSeparatedFromPackage() throws Exception {
        final String[] expected = {
            "13:78: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "import"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorMultipleImportEmptyClass.java"),
            expected);
    }

    @Test
    public void testImportSeparatedFromPackage() throws Exception {
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorImportSeparatedFromPackage.java"),
            expected);
    }

    @Test
    public void testBlockCommentNotSeparatedFromPackage() throws Exception {
        final String[] expected = {
            "14:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "/*"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorBlockCommentUnderPackage.java"),
            expected);
    }

    @Test
    public void testSingleCommentNotSeparatedFromPackage() throws Exception {
        final String[] expected = {
            "14:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "//"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorSingleCommentUnderPackage.java"),
            expected);
    }

    @Test
    public void testClassDefinitionNotSeparatedFromPackage() throws Exception {
        final String[] expected = {
            "14:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "CLASS_DEF"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorModifierUnderPackage.java"),
            expected);
    }

    @Test
    public void testCommentAfterPackageWithImports() throws Exception {
        final String[] expected = {
            "14:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "//"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorSingleLineCommentAfterPackage.java"),
                expected);
    }

    @Test
    public void testJavadocCommentAfterPackageWithImports() throws Exception {
        final DefaultConfiguration checkConfig = createModuleConfig(EmptyLineSeparatorCheck.class);
        final String[] expected = {
            "2:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "/*"),
        };
        verify(checkConfig,
                getPath("InputEmptyLineSeparatorJavadocCommentAfterPackage.java"),
                expected);
    }

    @Test
    public void testPackageImportsClassInSingleLine() throws Exception {
        final String[] expected = {
            "13:79: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "import"),
            "13:101: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "CLASS_DEF"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorPackageImportClassInOneLine.java"),
                expected);
    }

    @Test
    public void testEmptyLineAfterPackageForPackageAst() throws Exception {
        final String[] expected = {
            "12:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "/*"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorEmptyLineAfterPackageForPackageAst.java"),
                expected);
    }

    @Test
    public void testEmptyLineAfterPackageForImportAst() throws Exception {
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorEmptyLineAfterPackageForImportAst.java"),
                expected);
    }

    @Test
    public void testClassDefinitionAndCommentNotSeparatedFromPackage() throws Exception {
        final String[] expected = {
            "14:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "//"),
            "15:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "CLASS_DEF"),
        };
        final String testFile =
            "InputEmptyLineSeparatorClassDefinitionAndCommentNotSeparatedFromPackage.java";
        verifyWithInlineConfigParser(
                getPath(testFile), expected);
    }

    @Test
    public void testBlockCommentSeparatedFromPackage() throws Exception {
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorBlockCommentSeparatedFromPackage.java"),
            expected);
    }

    @Test
    public void testSingleCommentSeparatedFromPackage() throws Exception {
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorSingleCommentSeparatedFromPackage.java"),
            expected);
    }

    @Test
    public void testGetAcceptableTokens() {
        final EmptyLineSeparatorCheck emptyLineSeparatorCheckObj = new EmptyLineSeparatorCheck();
        final int[] actual = emptyLineSeparatorCheckObj.getAcceptableTokens();
        final int[] expected = {
            TokenTypes.PACKAGE_DEF,
            TokenTypes.IMPORT,
            TokenTypes.STATIC_IMPORT,
            TokenTypes.CLASS_DEF,
            TokenTypes.INTERFACE_DEF,
            TokenTypes.ENUM_DEF,
            TokenTypes.STATIC_INIT,
            TokenTypes.INSTANCE_INIT,
            TokenTypes.METHOD_DEF,
            TokenTypes.CTOR_DEF,
            TokenTypes.VARIABLE_DEF,
            TokenTypes.RECORD_DEF,
            TokenTypes.COMPACT_CTOR_DEF,
        };
        assertArrayEquals(expected, actual, "Default acceptable tokens are invalid");
    }

    @Test
    public void testPrePreviousLineEmptiness() throws Exception {
        final DefaultConfiguration checkConfig = createModuleConfig(EmptyLineSeparatorCheck.class);
        checkConfig.addProperty("allowMultipleEmptyLines", "false");
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verify(checkConfig,
            getPath("InputEmptyLineSeparatorPrePreviousLineEmptiness.java"), expected);
    }

    @Test
    public void testPrePreviousLineIsEmpty() throws Exception {
        final DefaultConfiguration checkConfig = createModuleConfig(EmptyLineSeparatorCheck.class);
        checkConfig.addProperty("allowMultipleEmptyLines", "false");
        final String[] expected = {
            "3:1: " + getCheckMessage(MSG_MULTIPLE_LINES, "package"),
        };
        verify(checkConfig,
                getPath("InputEmptyLineSeparatorPrePreviousLineIsEmpty.java"), expected);
    }

    @Test
    public void testPreviousLineEmptiness() throws Exception {
        final String[] expected = {
            "21:30: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
            "26:5: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
            "32:67: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
            "41:48: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
            "51:21: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorPreviousLineEmptiness.java"), expected);
    }

    @Test
    public void testDisAllowMultipleEmptyLinesInsideClassMembers() throws Exception {
        final String[] expected = {
            "37:11: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
            "49:11: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
            "55:11: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
            "60:35: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
            "65:11: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
            "72:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "CLASS_DEF"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorMultipleEmptyLinesInside.java"),
                expected);
    }

    @Test
    public void testAllowMultipleEmptyLinesInsideClassMembers() throws Exception {
        final String[] expected = {
            "91:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "CLASS_DEF"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorMultipleEmptyLinesInside2.java"),
                expected);
    }

    @Test
    public void testImportsAndStaticImports() throws Exception {
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorImports.java"), expected);
    }

    @Test
    public void testAllowPackageAnnotation() throws Exception {
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verifyWithInlineConfigParser(
                getPath("packageinfo/test1/package-info.java"),
                expected);
    }

    @Test
    public void testAllowJavadocBeforePackage() throws Exception {
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verifyWithInlineConfigParser(
                getPath("packageinfo/test2/package-info.java"),
                expected);
    }

    @Test
    public void testDisAllowBlockCommentBeforePackage() throws Exception {
        final String[] expected = {
            "15:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "package"),
        };
        verifyWithInlineConfigParser(
                getPath("packageinfo/test3/package-info.java"),
                expected);
    }

    @Test
    public void testAllowSingleLineCommentPackage() throws Exception {
        final String[] expected = {
            "16:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "package"),
        };
        verifyWithInlineConfigParser(
                getPath("packageinfo/test4/package-info.java"),
                expected);
    }

    @Test
    public void testNonPackageInfoWithJavadocBeforePackage() throws Exception {
        final String[] expected = {
            "15:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "package"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorNonPackageInfoWithJavadocBeforePackage.java"),
                expected);
    }

    @Test
    public void testClassOnly() throws Exception {
        final String[] expected = {
            "70:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "CLASS_DEF"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorMultipleEmptyLinesInside3.java"),
                expected);
    }

    @Test
    public void testLineSeparationBeforeComments() throws Exception {
        final String[] expected = {
            "31:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "package"),
            "35:1: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "39:1: " + getCheckMessage(MSG_MULTIPLE_LINES, "import"),
            "44:1: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "51:1: " + getCheckMessage(MSG_MULTIPLE_LINES, "/*"),
            "62:1: " + getCheckMessage(MSG_MULTIPLE_LINES, "/*"),
            "79:1: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "90:1: " + getCheckMessage(MSG_MULTIPLE_LINES, "/*"),
            "95:1: " + getCheckMessage(MSG_MULTIPLE_LINES, "import"),
            "101:1: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "105:1: " + getCheckMessage(MSG_MULTIPLE_LINES, "/*"),
            "113:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "VARIABLE_DEF"),
            "118:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "125:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "/*"),
            "138:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "/*"),
            "151:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "METHOD_DEF"),
            "158:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "168:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "/*"),
            "183:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "/*"),
            "200:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "CLASS_DEF"),
            "206:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "210:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "216:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "228:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "/*"),
            "241:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "/*"),
            "255:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "258:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "INTERFACE_DEF"),
            "263:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "/*"),
            "279:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "288:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "301:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "306:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "312:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "320:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "329:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "335:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "355:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
            "363:5: " + getCheckMessage(MSG_MULTIPLE_LINES, "//"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorWithComments.java"), expected);
    }

    @Test
    public void testIgnoreEmptyLinesBeforeCommentsWhenItIsAllowed() throws Exception {
        final String[] expected = {
            "31:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "package"),
            "258:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "INTERFACE_DEF"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorWithComments2.java"), expected);
    }

    @Test
    public void testNoViolationsOnEmptyLinesBeforeComments() throws Exception {
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorNoViolationOnEmptyLineBeforeComments.java"),
                expected);
    }

    @Test
    public void testEmptyLineSeparatorRecordsAndCompactCtors() throws Exception {
        final DefaultConfiguration checkConfig = createModuleConfig(EmptyLineSeparatorCheck.class);

        final String[] expected = {
            "2:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "package"),
            "15:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "RECORD_DEF"),
            "17:9: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "METHOD_DEF"),
            "18:9: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "COMPACT_CTOR_DEF"),
            "19:9: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "CTOR_DEF"),
            "20:9: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "STATIC_INIT"),
            "22:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "RECORD_DEF"),
            "23:5: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "CLASS_DEF"),
            "25:9: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "METHOD_DEF"),
            "26:9: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "CTOR_DEF"),
            "27:9: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "CTOR_DEF"),
            "32:9: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "COMPACT_CTOR_DEF"),
            "33:9: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "METHOD_DEF"),
            "38:9: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "COMPACT_CTOR_DEF"),
            "39:9: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "VARIABLE_DEF"),
        };

        verify(checkConfig,
                getNonCompilablePath("InputEmptyLineSeparatorRecordsAndCompactCtors.java"),
                expected);
    }

    @Test
    public void testEmptyLineSeparatorRecordsAndCompactCtorsNoEmptyLines() throws Exception {

        final String[] expected = {
            "14:1: " + getCheckMessage(MSG_SHOULD_BE_SEPARATED, "package"),
            "17:27: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
            "23:29: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
        };

        verifyWithInlineConfigParser(
                getNonCompilablePath(
                        "InputEmptyLineSeparatorRecordsAndCompactCtorsNoEmptyLines.java"),
                expected);
    }

    @Test
    public void testEmptyLineSeparatorMultipleSingleTypeVariables() throws Exception {

        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;

        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorSingleTypeVariables.java"),
                expected);
    }

    @Test
    public void testEmptyLineSeparatorEmptyLinesInsideClassMembersRecursive() throws Exception {
        final String[] expected = {
            "27:15: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorRecursive.java"),
                expected);
    }

    @Test
    public void testEmptyLineSeparatorNewMethodDef() throws Exception {
        final String[] expected = {
            "29:34: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
            "38:26: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorNewMethodDef.java"),
                expected);
    }

    @Test
    public void testEmptyLineSeparatorPostFixCornerCases() throws Exception {
        final String[] expected = {
            "18:19: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
            "32:29: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
            "43:29: " + getCheckMessage(MSG_MULTIPLE_LINES_INSIDE),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorPostFixCornerCases.java"),
                expected);
    }

    @Test
    public void testEmptyLineSeparatorAnnotation() throws Exception {
        final String[] expected = {
            "18:22: " + getCheckMessage(MSG_MULTIPLE_LINES_AFTER, "}"),
        };
        verifyWithInlineConfigParser(
                getPath("InputEmptyLineSeparatorAnnotations.java"),
                expected);
    }

}