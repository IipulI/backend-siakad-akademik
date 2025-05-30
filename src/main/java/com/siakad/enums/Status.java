package com.siakad.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    ACTIVE("Aktif"),
    INACTIVE("Tidak aktif"),
    ;


    private final String label;
}
