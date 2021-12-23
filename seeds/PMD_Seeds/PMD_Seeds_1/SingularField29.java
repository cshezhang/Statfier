
package com.foo;

import java.util.Optional;

public enum Supplier {
    bar("Bar");

    private final String supplierName;

    private Supplier(String supplierName) {
        this.supplierName = supplierName;
    }

    public static String getSupplierNameIfPresent(String supplier) {
        return Optional.ofNullable(supplier).map(foo -> valueOf(supplier).supplierName).orElse("");
    }
}
        