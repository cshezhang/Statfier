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

package com.puppycrawl.tools.checkstyle.checks.coding;

import static com.puppycrawl.tools.checkstyle.checks.coding.ParameterAssignmentCheck.MSG_KEY;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.AbstractModuleTestSupport;
import com.puppycrawl.tools.checkstyle.JavaParser;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.internal.utils.TestUtil;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

public class ParameterAssignmentCheckTest extends AbstractModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/puppycrawl/tools/checkstyle/checks/coding/parameterassignment";
    }

    @Test
    public void testDefault()
            throws Exception {
        final String[] expected = {
            "17:15: " + getCheckMessage(MSG_KEY, "field"),
            "18:15: " + getCheckMessage(MSG_KEY, "field"),
            "20:14: " + getCheckMessage(MSG_KEY, "field"),
            "28:30: " + getCheckMessage(MSG_KEY, "field1"),
            "45:31: " + getCheckMessage(MSG_KEY, "q"),
            "46:39: " + getCheckMessage(MSG_KEY, "q"),
            "47:34: " + getCheckMessage(MSG_KEY, "w"),
            "49:41: " + getCheckMessage(MSG_KEY, "w"),
            "50:49: " + getCheckMessage(MSG_KEY, "a"),
            "52:11: " + getCheckMessage(MSG_KEY, "c"),
            "53:11: " + getCheckMessage(MSG_KEY, "d"),
            "63:15: " + getCheckMessage(MSG_KEY, "d"),
        };
        verifyWithInlineConfigParser(
                getPath("InputParameterAssignmentWithUnchecked.java"),
               expected);
    }

    @Test
    public void testReceiverParameter() throws Exception {
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verifyWithInlineConfigParser(
                getPath("InputParameterAssignmentReceiver.java"), expected);
    }

    @Test
    public void testEnhancedSwitch() throws Exception {
        final String[] expected = {
            "14:28: " + getCheckMessage(MSG_KEY, "a"),
            "21:16: " + getCheckMessage(MSG_KEY, "result"),
        };
        verifyWithInlineConfigParser(
                getNonCompilablePath("InputParameterAssignmentWithEnhancedSwitch.java"),
                expected);
    }

    @Test
    public void testTokensNotNull() {
        final ParameterAssignmentCheck check = new ParameterAssignmentCheck();
        assertNotNull(check.getAcceptableTokens(), "Acceptable tokens should not be null");
        assertNotNull(check.getDefaultTokens(), "Default tokens should not be null");
        assertNotNull(check.getRequiredTokens(), "Required tokens should not be null");
    }

    /**
     * We cannot reproduce situation when visitToken is called and leaveToken is not.
     * So, we have to use reflection to be sure that even in such situation
     * state of the field will be cleared.
     *
     * @throws Exception when code tested throws exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testClearState() throws Exception {
        final ParameterAssignmentCheck check = new ParameterAssignmentCheck();
        final Optional<DetailAST> methodDef = TestUtil.findTokenInAstByPredicate(
            JavaParser.parseFile(new File(getPath("InputParameterAssignmentReceiver.java")),
                JavaParser.Options.WITHOUT_COMMENTS),
            ast -> ast.getType() == TokenTypes.METHOD_DEF);

        assertTrue(methodDef.isPresent(), "Ast should contain METHOD_DEF");
        assertTrue(
            TestUtil.isStatefulFieldClearedDuringBeginTree(check, methodDef.get(),
                "parameterNamesStack",
                parameterNamesStack -> ((Collection<Set<String>>) parameterNamesStack).isEmpty()),
                "State is not cleared on beginTree");
    }

}