
package com.example;

import not.lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExampleClass {

    @Builder.Default
    private final long exampleField = 0L;
}
        