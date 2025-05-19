package com.siakad.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageKey {
    SUCCESS("success"),
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

    CREATE_KELAS_KULIAH("Menambahkan data kelas kuliah"),
    UPDATE_KELAS_KULIAH("Mengubah data kelas kuliah"),
    DELETE_KELAS_KULIAH("Menghapus data kelas kuliah"),

    CREATE_JADWAL_KULIAH("Menambahkan data jadwal kuliah"),
    UPDATE_JADWAL_KULIAH("Mengubah data jadwal kuliah"),
    DELETE_JADWAL_KULIAH("Menghapus data jadwal kuliah"),

    CREATE_KOMPOSISI_PENILAIAN("Menambahkan data komposisi penilaian"),
    UPDATE_KOMPOSISI_PENILAIAN("Mengubah data komposisi penilaian"),
    DELETE_KOMPOSISI_PENILAIAN("Menghapus data komposisi penilaian"),

    CREATE_KOMPOSISI_NILAI_MATA_KULIAH("Menambahkan data komposisi nilai mata kuliah"),

    CREATE_JENJANG("Menambahkan data jenjang"),
    UPDATE_JENJANG("Mengubah data jenjang"),
    DELETE_JENJANG("Menghapus data jenjang"),

    CREATE_BATAS_SKS("Menambahkan data batas sks"),
    UPDATE_BATAS_SKS("Mengubah data batas sks"),
    DELETE_BATAS_SKS("Menghapus data batas sks"),

    UPDATE_SKALA_PENILAIAN("Mengubah data skala penilaian"),
    CREATE_SKALA_PENILAIAN("Menambahkan data skala penilaian"),
    DELETE_SKALA_PENILAIAN("Menghapus data skala penilaian"),

    CREATE_PROFIL_LULUSAN("Menambahkan data profil lulusan"),
    UPDATE_PROFIL_LULUSAN("Mengubah data profil lulusan"),
    DELETE_PROFIL_LULUSAN("Menghapus data profil lulusan"),

    CREATE_CAPAIAN_PEMBELAJARAN_LULUSAN("Menambahkan data capaian pembelajaran lulusan"),
    UPDATE_CAPAIAN_PEMBELAJARAN_LULUSAN("Mengubah data capaian pembelajaran lulusan"),
    DELETE_CAPAIAN_PEMBELAJARAN_LULUSAN("Menghapus data capaian pembelajaran lulusan"),

    CREATE_CAPAIAN_MATA_KULIAH("Menambahkan data capaian mata kuliah"),
    UPDATE_CAPAIAN_MATA_KULIAH("Mengubah data capaian mata kuliah"),
    DELETE_CAPAIAN_MATA_KULIAH("Menghapus data capaian mata kuliah"),

    CREATE_RPS("Menambahkan data rps"),
    UPDATE_RPS("Mengubah data rps"),
    DELETE_RPS("Menghapus data rps"),

    CREATE_PENGUMUMAN("Menambahkan data penguman"),
    UPDATE_PENGUMUMAN("Mengubah data penguman"),
    DELETE_PENGUMUMAN("Menghapus data penguman"),

    ;

    private final String message;

}
