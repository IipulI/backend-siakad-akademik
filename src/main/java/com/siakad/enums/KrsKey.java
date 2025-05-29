package com.siakad.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KrsKey {
    DRAFT("Draft"),
    DITOLAK("Ditolak"),
    MENUNGGU("Menunggu"),
    DISETUJUI("Disetujui"),

    BARU("Baru"),
    ULANG("Ulang"),

    LULUS("Lulus"),
    GAGAL("Tidak Lulus"),
    ;


    private final String label;
}
