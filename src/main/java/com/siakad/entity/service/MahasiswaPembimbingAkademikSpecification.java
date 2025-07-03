package com.siakad.entity.service;

import com.siakad.entity.KrsMahasiswa;
import com.siakad.entity.Mahasiswa;
import com.siakad.entity.PembimbingAkademik;
import com.siakad.entity.PeriodeAkademik;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MahasiswaPembimbingAkademikSpecification {

    public static Specification<Mahasiswa> build(String programStudi, String periodeAkademik, UUID periodeAkademikId, UUID dosenId, String namaMahasiswa,
                                                 String angkatan, String statusMahasiswa, String statusKrs,
                                                 Boolean hasPembimbing) {

        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            if (periodeAkademik != null) {
                Subquery<String> periodeSubquery = query.subquery(String.class);
                Root<PeriodeAkademik> periodeRoot = periodeSubquery.from(PeriodeAkademik.class);
                periodeSubquery.select(periodeRoot.get("kodePeriode"));
                periodeSubquery.where(criteriaBuilder.equal(periodeRoot.get("namaPeriode"), periodeAkademik));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("periodeMasuk"), periodeSubquery));
            }
            else if (periodeAkademikId != null) {
                Subquery<String> periodeSubquery = query.subquery(String.class);
                Root<PeriodeAkademik> periodeRoot = periodeSubquery.from(PeriodeAkademik.class);
                periodeSubquery.select(periodeRoot.get("kodePeriode"));
                periodeSubquery.where(criteriaBuilder.equal(periodeRoot.get("id"), periodeAkademikId));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("periodeMasuk"), periodeSubquery));
            }

            if (programStudi != null) {
                predicates.add(criteriaBuilder.equal(root.get("siakProgramStudi").get("namaProgramStudi"), programStudi));
            }
            if (dosenId != null) {
                Subquery<Integer> dosenSubquery = query.subquery(Integer.class);
                Root<PembimbingAkademik> paRoot = dosenSubquery.from(PembimbingAkademik.class);
                dosenSubquery.select(criteriaBuilder.literal(1));

                dosenSubquery.where(
                        criteriaBuilder.equal(paRoot.get("siakMahasiswa"), root),
                        criteriaBuilder.equal(paRoot.get("siakDosen").get("id"), dosenId)
                );
                predicates.add(criteriaBuilder.exists(dosenSubquery));
            }
            if (StringUtils.hasText(angkatan)) {
                predicates.add(criteriaBuilder.equal(root.get("angkatan"), angkatan));
            }
            if (StringUtils.hasText(statusMahasiswa)) {
                predicates.add(criteriaBuilder.equal(root.get("statusMahasiswa"), statusMahasiswa));
            }
            if (StringUtils.hasText(namaMahasiswa)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nama")), "%" + namaMahasiswa.toLowerCase() + "%"));
            }

            if (StringUtils.hasText(statusKrs)) {
                Subquery<Integer> krsSubquery = query.subquery(Integer.class);
                krsSubquery.select(criteriaBuilder.literal(1));
                krsSubquery.where(
                        criteriaBuilder.equal(krsSubquery.from(KrsMahasiswa.class).get("siakMahasiswa"), root),
                        criteriaBuilder.equal(krsSubquery.from(KrsMahasiswa.class).get("siakPeriodeAkademik").get("namaPeriode"), periodeAkademik)
                );

                if ("Diajukan".equalsIgnoreCase(statusKrs)) {
                    krsSubquery.where(krsSubquery.getRestriction(), criteriaBuilder.notEqual(krsSubquery.from(KrsMahasiswa.class).get("status"), "Diajukan"));
                    predicates.add(criteriaBuilder.exists(krsSubquery));
                }
                else if ("Disetujui".equalsIgnoreCase(statusKrs)) {
                    krsSubquery.where(krsSubquery.getRestriction(), criteriaBuilder.equal(krsSubquery.from(KrsMahasiswa.class).get("status"), "Disetujui"));
                    predicates.add(criteriaBuilder.exists(krsSubquery));
                }
            }

            if (hasPembimbing != null) {
                Subquery<Integer> paSubquery = query.subquery(Integer.class);
                Root<PembimbingAkademik> paRoot = paSubquery.from(PembimbingAkademik.class);
                paSubquery.select(criteriaBuilder.literal(1));

                List<Predicate> subqueryPredicates = new ArrayList<>();

                subqueryPredicates.add(criteriaBuilder.equal(paRoot.get("siakMahasiswa"), root));

                if (periodeAkademikId != null) {
                    subqueryPredicates.add(criteriaBuilder.equal(
                            paRoot.get("siakPeriodeAkademik").get("id"),
                            periodeAkademikId
                    ));
                } else if (StringUtils.hasText(periodeAkademik)) {
                    subqueryPredicates.add(criteriaBuilder.equal(
                            paRoot.get("siakPeriodeAkademik").get("namaPeriode"),
                            periodeAkademik
                    ));
                }

                paSubquery.where(criteriaBuilder.and(subqueryPredicates.toArray(new Predicate[0])));

                predicates.add(hasPembimbing ? criteriaBuilder.exists(paSubquery) : criteriaBuilder.not(criteriaBuilder.exists(paSubquery)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}