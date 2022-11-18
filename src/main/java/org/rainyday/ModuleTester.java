package org.rainyday;

import net.sourceforge.pmd.PMD;
import org.junit.Test;
import org.rainyday.analysis.TypeWrapper;

public class ModuleTester {

    @Test
    public void testAST() {
        TypeWrapper wrapper = new TypeWrapper("src/test/java/Foo.java", "");
        wrapper.printBasicInfo();
    }

    @Test
    public void testPMD() {
        String[] pmdArgs = {
                "-d", "./TestCase/Case1.java",
//                "-R", "./PMD_config.xml",
//                "-R", "category/java/design.xml/SingularField",
//                "-R", "category/java/design.xml/ClassWithOnlyPrivateConstructorsShouldBeFinal",
//                "-R", "category/java/errorprone.xml/TestClassWithoutTestCases",
//                "-R", "category/java/codestyle.xml/AvoidProtectedMethodInFinalClassNotExtending",
//                "-R", "category/java/bestpractices.xml/LooseCoupling",
//                "-R", "category/java/bestpractices.xml/LiteralsFirstInComparisons",
//                "-R", "category/java/errorprone.xml/FinalizeDoesNotCallSuperFinalize",
//                "-R", "category/java/design.xml/ExcessivePublicCount",
//                "-R", "category/java/design.xml/UseUtilityClass",
//                "-R", "category/java/codestyle.xml/UnnecessaryReturn",
//                "-R", "category/java/codestyle.xml/UnnecessaryImport",
//                "-R", "category/java/codestyle.xml/UnnecessaryFullyQualifiedName",
//                "-R", "category/java/codestyle.xml/UnnecessaryConstructor",
//                "-R", "category/java/design.xml/SimplifyBooleanReturns",
//                "-R", "category/java/design.xml/LawOfDemeter",
//                "-R", "category/java/errorprone.xml/MissingStaticMethodInNonInstantiatableClass",
//                "-R", "category/java/codestyle.xml/LinguisticNaming",
//                "-R", "category/java/codestyle.xml/EmptyMethodInAbstractClassShouldBeAbstract",
//                "-R", "category/java/codestyle.xml/EmptyControlStatement",
//                "-R", "category/java/multithreading.xml/DoNotUseThreads",
//                "-R", "category/java/design.xml/DataClass",
//                "-R", "category/java/codestyle.xml/CommentDefaultAccessModifier",
//                "-R", "category/java/errorprone.xml/BeanMembersShouldSerialize",
//                "-R", "category/java/bestpractices.xml/UnusedPrivateField",
//                "-R", "category/java/design.xml/ImmutableField",
//                "-R", "category/java/bestpractices.xml/UnusedFormalParameter",
//                "-R", "category/java/performance.xml/UseArrayListInsteadOfVector",
//                "-R", "category/java/errorprone.xml/DetachedTestCase",
//                "-R", "category/java/codestyle.xml/UnnecessaryLocalBeforeReturn",
//                "-R", "category/java/documentation.xml/CommentSize",
//                "-R", "category/java/bestpractices.xml/UnusedAssignment",
//                "-R", "category/java/performance.xml/RedundantFieldInitializer",
//                "-R", "category/java/performance.xml/UselessStringValueOf",
//                "-R", "category/java/bestpractices.xml/SimplifiableTestAssertion",
//                "-R", "category/java/errorprone.xml/CompareObjectsWithEquals",
//                "-R", "category/java/performance.xml/AvoidArrayLoops",
//                "-R", "category/java/performance.xml/UseStringBufferLength",
//                "-R", "category/java/performance.xml/InefficientEmptyStringCheck",
//                "-R", "category/java/errorprone.xml/UseEqualsToCompareStrings",
//                "-R", "category/java/errorprone.xml/NullAssignment",
//                "-R", "rulesets/java/quickstart.xml",
//                "-R", "category/java/design.xml/AbstractClassWithoutAnyMethod",
//                "-R", "category/java/bestpractices.xml/AccessorClassGeneration",
//                "-R", "category/java/bestpractices.xml/AccessorMethodGeneration",
//                "-R", "category/java/performance.xml",
//                "-R", "category/java/security.xml",
//                "-R", "category/java/bestpractices.xml/UnusedLocalVariable",
//                "-R", "category/java/bestpractices.xml/AvoidUsingHardCodedIP",
//                "-R", "category/java/bestpractices.xml/WhileLoopWithLiteralBoolean",
//                "-R", "category/java/codestyle.xml/UnnecessaryLocalBeforeReturn",
//                "-R", "category/java/multithreading.xml/DoubleCheckedLocking",
//                "-R", "category/java/multithreading.xml/DontCallThreadRun",
//                "-R", "category/java/performance.xml/AvoidCalendarDateCreation",
//                "-R", "category/java/performance.xml/AppendCharacterWithChar",
//                "-R", "category/java/performance.xml/UseStringBufferLength",
//                "-R", "category/java/performance.xml/UseStringBufferForStringAppends",
//                "-R", "category/java/performance.xml/UseIndexOfChar",
//                "-R", "category/java/performance.xml/OptimizableToArrayCall",
//                "-R", "category/java/performance.xml/InefficientStringBuffering",
//                "-R", "category/java/performance.xml/AvoidInstantiatingObjectsInLoops",
//                "-R", "category/java/performance.xml/AvoidFileStream",
//                "-R", "category/java/performance.xml/ConsecutiveAppendsShouldReuse",
//                "-R", "category/java/performance.xml/InsufficientStringBufferDeclaration",
//                "-R", "category/java/performance.xml/UselessStringValueOf",
//                "-R", "category/java/performance.xml/UseArraysAsList",
                "-R", "category/java/security.xml/HardCodedCryptoKey",
//                "-R", "category/java/security.xml/InsecureCryptoIv",
//                "-R", "category/java/errorprone.xml/AvoidFieldNameMatchingMethodName",
                "-f", "html",
                "-r", "./PMD_Result.html",
//                "--debug",
                "--no-cache"

        };
        PMD.main(pmdArgs);
    }



}

