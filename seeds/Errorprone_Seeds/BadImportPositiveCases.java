/*
 * Copyright 2018 The Error Prone Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.errorprone.bugpatterns.testdata;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Tests for {@link BadImport}.
 *
 * @author awturner@google.com (Andy Turner)
 */
class BadImportPositiveCases {
  public void variableDeclarations() {
    // Only the first match is reported; but all occurrences are fixed.
    // BUG: Diagnostic contains: ImmutableList.Builder
    Builder<String> qualified;
    Builder raw;
  }

  public void variableDeclarationsNestedGenerics() {
    Builder<Builder<String>> builder1;
    Builder<Builder> builder1Raw;
    ImmutableList.Builder<Builder<String>> builder2;
    ImmutableList.Builder<Builder> builder2Raw;
  }

  @Nullable
  Builder<@Nullable Builder<@Nullable String>> parameterizedWithTypeUseAnnotationMethod() {
    return null;
  }

  public void variableDeclarationsNestedGenericsAndTypeUseAnnotations() {

    @Nullable Builder<@Nullable String> parameterizedWithTypeUseAnnotation1;

    @Nullable Builder<@Nullable Builder<@Nullable String>> parameterizedWithTypeUseAnnotation2;
  }

  public void newClass() {
    new Builder<String>();
    new Builder<Builder<String>>();
  }

  Builder<String> returnGenericExplicit() {
    return new Builder<String>();
  }

  Builder<String> returnGenericDiamond() {
    return new Builder<>();
  }

  Builder returnRaw() {
    return new Builder();
  }

  void classLiteral() {
    System.out.println(Builder.class);
  }
}
