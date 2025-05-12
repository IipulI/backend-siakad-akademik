package com.siakad.enums;


import lombok.Getter;

@Getter
public enum PeriodeAkademikStatus {
    AKTIF("Aktif"),
    NONAKTIF("Nonaktif");

    private final String label;

    PeriodeAkademikStatus(String label) {
        this.label = label;
    }
}
