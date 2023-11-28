package com.example;

import lombok.Data;
import not.lombok.Builder;

@Data
@Builder
public class ExampleClass {

  @Builder.Default private final long exampleField = 0L;
}

