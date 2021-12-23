
package com.example;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExampleClass {

    @Builder.Default
    private final long exampleField = 0L;
}
        