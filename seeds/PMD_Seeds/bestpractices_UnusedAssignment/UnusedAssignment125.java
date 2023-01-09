
            import Something;

            class Foo {
                int method() {
                    try (Something ignored = Something.create()) {
                        // even if ignored is unused, it won't be flagged
                        // its purpose might be to side-effect in the create/close routines

                    } catch (Exception e) { // this is unused and will cause a warning if `reportUnusedVariables` is true
                        // you should choose a name that starts with "ignored"
                        return;
                    }
                }
            }

        