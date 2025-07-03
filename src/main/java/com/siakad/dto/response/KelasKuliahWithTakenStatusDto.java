package com.siakad.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
// import java.time.LocalDateTime; // This import might no longer be needed
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class KelasKuliahWithTakenStatusDto {

    // Fields from KelasKuliah (kl)
    private UUID id;
    private String namaKelas;
    private Integer kapasitas;
    private String sistemKuliah;
    private String statusKelas;
    private Integer jumlahPertemuan;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
    // REMOVED: private LocalDateTime kelasKuliahCreatedAt; // This was here before

    // Fields from MataKuliah (mk)
    private UUID mataKuliahId;
    private Integer semesterMataKuliah;
    private String kodeMataKuliah;
    private String namaMataKuliah;
    private String jenisMataKuliah;
    private String nilaiMin;
    private Integer sksTatapMuka;
    private Integer sksPraktikum;
    private Boolean adaPraktikum;
    private Boolean opsiMataKuliah;
    // REMOVED: private LocalDateTime mataKuliahCreatedAt; // This was here before
    // REMOVED: private LocalDateTime mataKuliahUpdatedAt; // This was here before

    // Fields from JadwalKuliah (jkl)
    private String hari;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private String dosenPengajar;

    // Fields from PeriodeAkademik (pa) and TahunAjaran (sa)
    private UUID periodeAkademikId;
    private String periodeAkademikNama;
    private String tahunAjaran;

    // Fields for ProgramStudi and TahunKurikulum related to MataKuliah
    private String programStudiNama;
    private String tahunKurikulumTahun;

    // New flag: true if the current student has taken this MataKuliah (course) before
    private boolean isTakenByCurrentUser;

    // New field: the 'nilaiAkhir' from the last time the course was taken by the current student
    private String lastTakenNilai;


    // --- CRITICAL: THE EXPLICIT CONSTRUCTOR MUST MATCH THE ERROR LOG'S SELECT NEW ---
    public KelasKuliahWithTakenStatusDto(
            // KelasKuliah (kl) - 8 fields
            UUID id,
            String namaKelas,
            Integer kapasitas,
            String sistemKuliah,
            String statusKelas,
            Integer jumlahPertemuan,
            LocalDate tanggalMulai,
            LocalDate tanggalSelesai,

            // MataKuliah (mk) - 10 fields
            UUID mataKuliahId,
            Integer semesterMataKuliah,
            String kodeMataKuliah,
            String namaMataKuliah,
            String jenisMataKuliah,
            String nilaiMin,
            Integer sksTatapMuka,
            Integer sksPraktikum,
            Boolean adaPraktikum,
            Boolean opsiMataKuliah,

            // JadwalKuliah (jkl) and Dosen (d) - 4 fields
            String hari,
            LocalTime jamMulai,
            LocalTime jamSelesai,
            String dosenPengajar,

            // PeriodeAkademik (pa) and TahunAjaran (sa) - 3 fields
            UUID periodeAkademikId,
            String periodeAkademikNama,
            String tahunAjaran,

            // ProgramStudi (ps) and TahunKurikulum (tk) - 2 fields
            String programStudiNama,
            String tahunKurikulumTahun,

            // Calculated Fields - 2 fields
            boolean isTakenByCurrentUser,
            String lastTakenNilai
    ) {
        // Assign all parameters to their respective fields
        this.id = id;
        this.namaKelas = namaKelas;
        this.kapasitas = kapasitas;
        this.sistemKuliah = sistemKuliah;
        this.statusKelas = statusKelas;
        this.jumlahPertemuan = jumlahPertemuan;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;

        this.mataKuliahId = mataKuliahId;
        this.semesterMataKuliah = semesterMataKuliah;
        this.kodeMataKuliah = kodeMataKuliah;
        this.namaMataKuliah = namaMataKuliah;
        this.jenisMataKuliah = jenisMataKuliah;
        this.nilaiMin = nilaiMin;
        this.sksTatapMuka = sksTatapMuka;
        this.sksPraktikum = sksPraktikum;
        this.adaPraktikum = adaPraktikum;
        this.opsiMataKuliah = opsiMataKuliah;

        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.dosenPengajar = dosenPengajar;

        this.periodeAkademikId = periodeAkademikId;
        this.periodeAkademikNama = periodeAkademikNama;
        this.tahunAjaran = tahunAjaran;

        this.programStudiNama = programStudiNama;
        this.tahunKurikulumTahun = tahunKurikulumTahun;

        this.isTakenByCurrentUser = isTakenByCurrentUser;
        this.lastTakenNilai = lastTakenNilai;
    }
}