package com.siakad.enums;

public enum InvoiceKey {
    // tahap
    TAHAP1("1"),
    TAHAP2("2"),

    // Metode bayar
    MANUAL("manual"),
    TRANSFER("transfer"),
    // Status
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

