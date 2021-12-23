

            import static unknown.K.*;

            class Foo {
                {
                    for (String s : someExpression()) {

                    }

                    for (final String s : someExpression()) {

                    }

                    for (@Annot final String s : someExpression()) {

                    }

                    for (final @Annot String s : someExpression()) {

                    }
                }
            }

        