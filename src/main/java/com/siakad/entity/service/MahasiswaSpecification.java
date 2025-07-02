package com.siakad.entity.service;

import com.siakad.entity.InvoicePembayaranKomponenMahasiswa;
import com.siakad.entity.Mahasiswa;
import com.siakad.util.QuerySpecification;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

public class MahasiswaSpecification extends QuerySpecification<Mahasiswa> {

    private static boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        return str.trim().matches("\\d+");
    }

    private Specification<Mahasiswa> byProgramStudi(String param) {
        return attributeContains("siakProgramStudi.namaProgramStudi", param);
    }

    private Specification<Mahasiswa> byNpm(String param) {
        return attributeContains("npm", param);
    }

    private Specification<Mahasiswa> byIsDeleted() {
        return attributeEqual("isDeleted", false);
    }

    private Specification<Mahasiswa> byFakultas(String param) {
        return attributeContains("siakProgramStudi.siakFakultas.namaFakultas", param);
    }

    private Specification<Mahasiswa> byPeriodeMasuk(String param) {
        return attributeContains("periodeMasuk", param);
    }

    private Specification<Mahasiswa> bySistemKuliah(String param) {
        return attributeContains("sistemKuliah", param);
    }

    private Specification<Mahasiswa> byAngkatan(String param) {
        return attributeContains("angkatan", param);
    }

    private Specification<Mahasiswa> bySemester(Integer param) {
        return attributeEqual("semester", param);
    }

    private Specification<Mahasiswa> byNama(String param) {
        return attributeContains("nama", param);
    }

    private Specification<Mahasiswa> byJenisPendaftaran(String param) {
        return attributeContains("jenisPendaftaran", param);
    }
    private Specification<Mahasiswa> byJalurPendaftaran(String param){
        return attributeContains("jalurPendaftaran", param);
    }
    private Specification<Mahasiswa> byStatusMahasiswa(String param){
        return attributeContains("statusMahasiswa", param);
    }
    private Specification<Mahasiswa> byGelombang(String param){
        return attributeContains("gelombang", param);
    }
    private Specification<Mahasiswa> byJenisKelamin(String param){
        return attributeContains("jenisKelamin", param);
    }
    private Specification<Mahasiswa> byPeriodeKeluar(String param){
        return attributeContains("periodeKeluar", param);
    }

    private Specification<Mahasiswa> byKurikulum(String param){
        return attributeContains("kurikulum", param);
    }

    public Specification<Mahasiswa> entitySearch(String keyword, String fakultas, String periodeMasuk, String sistemKuliah, String angkatan, Integer semester, String programStudi, String jenisPendaftaran, String jalurPendaftaran, String statusMahasiswa, String gelombang, String jenisKelamin, String kurikulum, String periodeKeluar) {
        Specification<Mahasiswa> spec = byIsDeleted();

        if (!Strings.isBlank(programStudi)){
            spec = spec.and(byProgramStudi(programStudi));
        }

        if(!Strings.isBlank(fakultas)){
            spec = spec.and(byFakultas(fakultas));
        }

        if(!Strings.isBlank(periodeMasuk)){
            spec = spec.and(byPeriodeMasuk(periodeMasuk));
        }

        if(!Strings.isBlank(sistemKuliah)){
            spec = spec.and(bySistemKuliah(sistemKuliah));
        }

        if(!Strings.isBlank(angkatan)){
            spec = spec.and(byAngkatan(angkatan));
        }

        if (semester != null) {
            spec = spec.and(bySemester(semester));
        }

        if(jenisPendaftaran != null){
            spec = spec.and(byJenisPendaftaran(jenisPendaftaran));
        }

        if(jalurPendaftaran != null) {
            spec = spec.and(byJalurPendaftaran(jalurPendaftaran));
        }

        if(statusMahasiswa != null) {
            spec = spec.and(byStatusMahasiswa(statusMahasiswa));
        }

        if(gelombang != null) {
            spec = spec.and(byGelombang(gelombang));
        }

        if(jenisKelamin != null) {
            spec = spec.and(byJenisKelamin(jenisKelamin));
        }

        if(periodeKeluar != null) {
            spec = spec.and(byPeriodeKeluar(periodeKeluar));
        }

        if(kurikulum != null) {
            spec = spec.and(byKurikulum(kurikulum));
        }

//        if (!Strings.isBlank(npm)){
//            spec = spec.and(byNpm(npm));
//        }

        if(!Strings.isBlank(keyword)){
            String trimmedKeyword = keyword.trim();

            if (isNumeric(trimmedKeyword)) {
                spec = spec.and(byNpm(trimmedKeyword));
            }
            else {
                spec = spec.and(byNama(trimmedKeyword));
            }
        }

        return spec;
    }

}
