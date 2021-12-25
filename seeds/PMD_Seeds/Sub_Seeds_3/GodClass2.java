
package com.pack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pack implements IPack {

    @Override
    public Map<String, String> get() {

        class Inner implements IInner {

            private Map<String, String> results;

            public Inner(Map<String, String> results) {
                this.results = results;
            }
        }
        return null;
    }
}
        