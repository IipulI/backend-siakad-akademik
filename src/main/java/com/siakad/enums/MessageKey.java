package com.siakad.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageKey {
    SUCCESS("berhasil"),
    LOGIN_SUCCESS("Berhasil masuk"),
    CREATED("Berhasil tambah data"),
    UPDATED("Berhasil memperbarui data"),
    READ("Berhasil mengambil data"),
    DELETED("Berhasil menghapus data"),

    CREATE_MAHASISWA("Menambahkan data mahasiswa"),
    UPDATE_MAHASISWA("Memperbarui data mahasiswa"),
    DELETE_MAHASISWA("Menghapus data mahasiswa"),

    CREATE_KELUARGA_MAHASISWA("Menambahkan data keluarga mahasiswa"),
    UPDATE_KELUARGA_MAHASISWA("Memperbarui data keluarga mahasiswa"),
    DELETE_KELUARGA_MAHASISWA("Menghapus data keluarga mahasiswa"),

    CREATE_INVOICE_MAHASISWA("Menambahkan data invoice mahasiswa"),

    CREATE_INVOICE_KOMPONEN_MAHASISWA("Menambahkan data invoice komponen mahasiswa"),
    UPDATE_INVOICE_KOMPONEN_MAHASISWA("Mengubah data invoice komponen mahasiswa"),
    DELETE_INVOICE_KOMPONER_MAHASISWA("Menghapus data invoice komponen mahasiswa"),

    CREATE_KOMPONEN_PENILAIAN("Menambahkan data komponen penilaian"),
    UPDATE_KOMPONEN_PENILAIAN("Mengubah data komponen penilaian"),
    DELETE_KOMPONEN_PENILAIAN("Menghapus data komponen penilaian"),

    CREATE_TAHUN_AJARAN("Menambahkan data tahun ajaran"),
    UPDATE_TAHUN_AJARAN("Mengubah data tahun ajaran"),
    DELETE_TAHUN_AJARAN("Menghapus data tahun ajaran"),

    CREATE_PERIODE_AKADEMIK("Menambahkan data periode akademik"),
    UPDATE_PERIODE_AKADEMIK("Mengubah data periode akademik"),
    DELETE_PERIODE_AKADEMIK("Menghapus data periode akademik"),

    CREATE_TAHUN_KURIKULUM("Menambahkan data tahun kurikulum"),
    UPDATE_TAHUN_KURIKULUM("Mengubah data tahun kurikulum"),
    DELETE_TAHUN_KURIKULUM("Menghapus data tahun kurikulum"),

    CREATE_MATA_KULIAH("Menambahkan data mata kuliah"),
    UPDATE_MATA_KULIAH("Mengubah data mata kuliah"),
    DELETE_MATA_KULIAH("Menghapus data mata kuliah"),

    ;

    private final String message;

}
