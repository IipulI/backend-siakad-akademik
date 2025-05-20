package com.siakad.enums;

public enum InvoiceKey {

    TAHAP1("1"),
    TAHAP2("2"),

    MANUAL("manual"),
    TRANSFER("transfer"),

    BELUM_LUNAS("belum lunas"),
    LUNAS("lunas");

    private final String label;

    InvoiceKey(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

