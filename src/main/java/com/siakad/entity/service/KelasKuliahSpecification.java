package com.siakad.entity.service;

import com.siakad.entity.Dosen;
import com.siakad.entity.JadwalKuliah;
import com.siakad.entity.KelasKuliah;
import com.siakad.entity.MataKuliah;
import com.siakad.util.QuerySpecification;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class KelasKuliahSpecification extends QuerySpecification<KelasKuliah> {

    private Specification<KelasKuliah> byPeriodeAkademik(String param) {
        return attributeContains("siakPeriodeAkademik.namaPeriode", param);
    }

    private Specification<KelasKuliah> byKurikulum(String param) {
        return attributeContains("siakMataKuliah.siakTahunKurikulum.tahun", param);
    }

    private Specification<KelasKuliah> byMataKuliah(String param) {
        return attributeContains("siakMataKuliah.namaMataKuliah", param);
    }

    private Specification<KelasKuliah> byProgramStudi(String param) {
        return attributeContains("siakProgramStudi.namaProgramStudi", param);
    }

    private Specification<KelasKuliah> byDosen(String dosen) {
        return (root, query, criteriaBuilder) -> {
            // Join KelasKuliah to JadwalKuliah
            Join<KelasKuliah, JadwalKuliah> jadwalKuliahJoin = root.join("siakJadwalKuliah", JoinType.INNER);
            // Join JadwalKuliah to Dosen
            Join<JadwalKuliah, Dosen> dosenJoin = jadwalKuliahJoin.join("siakDosen", JoinType.INNER);

            // You must select distinct roots if a KelasKuliah can have multiple JadwalKuliah
            // entries with different Dosen, and you want each KelasKuliah only once.
            // This needs to be handled at the query level, not strictly in the predicate
            // of the Specification itself, but it's good to keep in mind.
            // For now, the predicate will filter by the Dosen name.
            query.distinct(true); // Ensures distinct KelasKuliah results

            // Build the LIKE predicate for the Dosen's nama attribute (case-insensitive)
            return criteriaBuilder.like(
                    criteriaBuilder.lower(dosenJoin.get("nama")),
                    "%" + dosen.toLowerCase() + "%"
            );
        };
    }

    private Specification<KelasKuliah> bySistemKuliah(String param) {
        return attributeContains("sistemKuliah", param);
    }

    private Specification<KelasKuliah> byNama(String param) {
        return attributeContains("nama", param);
    }

    private Specification<KelasKuliah> notDeleted() {
        return attributeEqual("isDeleted", false);
    }

    public  Specification<KelasKuliah> entitySearch(String keyword,
                                                    String periodeAkademik,
                                                    String tahunKuriKulum,
                                                    String programStudi,
                                                    String dosen,
                                                    String sistemKuliah
                                                    ){
        Specification<KelasKuliah> spec = notDeleted();

        if (!Strings.isBlank(programStudi)){
            spec = spec.and(byProgramStudi(programStudi));
        }

        if (!Strings.isBlank(periodeAkademik)){
            spec = spec.and(byPeriodeAkademik(periodeAkademik));
        }

        if (!Strings.isBlank(sistemKuliah)){
            spec = spec.and(bySistemKuliah(sistemKuliah));
        }

        if (!Strings.isBlank(tahunKuriKulum)){
            spec = spec.and(byKurikulum(tahunKuriKulum));
        }

        if (!Strings.isBlank(keyword)) {
            spec = spec.and(
                    Specification.where(byNama(keyword))
            );
        }

        if (!Strings.isBlank(dosen)) {
            spec = spec.and(byDosen(dosen));
        }

        return spec;
    }


    public  Specification<KelasKuliah> entitySearchKelas(String mataKuliah,
                                                         String programStudi,
                                                         String periodeAkademik
    ){
        Specification<KelasKuliah> spec = notDeleted();

        if (!Strings.isBlank(mataKuliah)){
            spec = spec.and(byMataKuliah(mataKuliah));
        }

        if (!Strings.isBlank(programStudi)){
            spec = spec.and(byProgramStudi(programStudi));
        }

        if (!Strings.isBlank(periodeAkademik)){
            spec = spec.and(byPeriodeAkademik(periodeAkademik));
        }

        return spec;
    }
}
