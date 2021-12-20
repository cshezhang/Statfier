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
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Tests for {@link BadImport}.
 *
 * @author awturner@google.com (Andy Turner)
 */
class BadImportPositiveCases {
  public void variableDeclarations() {
    ImmutableList.Builder<String> qualified;
    ImmutableList.Builder raw;
  }

  public void variableDeclarationsNestedGenerics() {
    ImmutableList.Builder<ImmutableList.Builder<String>> builder1;
    ImmutableList.Builder<ImmutableList.Builder> builder1Raw;
    ImmutableList.Builder<ImmutableList.Builder<String>> builder2;
    ImmutableList.Builder<ImmutableList.Builder> builder2Raw;
  }

  ImmutableList.@Nullable Builder<ImmutableList.@Nullable Builder<@Nullable String>>
      parameterizedWithTypeUseAnnotationMethod() {
    return null;
  }

  public void variableDeclarationsNestedGenericsAndTypeUseAnnotations() {

    ImmutableList.@Nullable Builder<@Nullable String> parameterizedWithTypeUseAnnotation1;

    ImmutableList.@Nullable Builder<ImmutableList.@Nullable Builder<@Nullable String>>
        parameterizedWithTypeUseAnnotation2;
  }

  public void newClass() {
    new ImmutableList.Builder<String>();
    new ImmutableList.Builder<ImmutableList.Builder<String>>();
  }

  ImmutableList.Builder<String> returnGenericExplicit() {
    return new ImmutableList.Builder<String>();
  }

  ImmutableList.Builder<String> returnGenericDiamond() {
    return new ImmutableList.Builder<>();
  }

  ImmutableList.Builder returnRaw() {
    return new ImmutableList.Builder();
  }

  void classLiteral() {
    System.out.println(ImmutableList.Builder.class);
  }
}
