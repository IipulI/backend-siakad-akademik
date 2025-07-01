package com.siakad.entity.service;

import com.siakad.entity.MataKuliah;
import com.siakad.util.QuerySpecification;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.UUID;

public class MataKuliahSpecification extends QuerySpecification<MataKuliah> {

    private Specification<MataKuliah> byTahunKurikulum(String tahunKurikulum) {
        return attributeContains("siakTahunKurikulum.tahun", tahunKurikulum);
    }

    private Specification<MataKuliah> byProgramStudi(String programStudi) {
        return attributeContains("siakProgramStudi.namaProgramStudi", programStudi);
    }

    private Specification<MataKuliah> byJenisMataKuliah(String jenisMataKuliah) {
        return attributeContains("jenisMataKuliah", jenisMataKuliah);
    }

    private Specification<MataKuliah> byMataKuliah(String namaMataKuliah) {
        return attributeContains("namaMataKuliah", namaMataKuliah);
    }

    private Specification<MataKuliah> byKodeMataKuliah(String kodeMataKuliah) {
        return attributeContains("kodeMataKuliah", kodeMataKuliah);
    }

    private Specification<MataKuliah> byUuidsIn(Collection<UUID> uuids) {
        // Ensure uuids is not null or empty before attempting to create the 'in' clause
        if (uuids == null || uuids.isEmpty()) {
            return Specification.where(null); // Return a no-op specification if no UUIDs provided
        }
        return (root, query, criteriaBuilder) -> root.get("id").in(uuids);
    }

    private Specification<MataKuliah> notDeleted() {
        return attributeEqual("isDeleted", false);
    }

    public Specification<MataKuliah> entitySearch(String keyword,
                                                  String programStudi,
                                                  String jenisMataKuliah,
                                                  String tahunKurikulum,
                                                  Collection<UUID> mataKuliahUuids
                                                  ) {
        Specification<MataKuliah> spec = notDeleted();

        if (!Strings.isBlank(programStudi)){
            spec = spec.and(byProgramStudi(programStudi));
        }

        if (!Strings.isBlank(tahunKurikulum)){
            spec = spec.and(byTahunKurikulum(tahunKurikulum));
        }

        if (!Strings.isBlank(jenisMataKuliah)){
            spec = spec.and(byJenisMataKuliah(jenisMataKuliah));
        }

        if (!Strings.isBlank(keyword)) {
            spec = spec.and(
                    Specification.where(byMataKuliah(keyword))
                            .or(byKodeMataKuliah(keyword))
            );
        }

        if (mataKuliahUuids != null && !mataKuliahUuids.isEmpty()) {
            spec = spec.and(byUuidsIn(mataKuliahUuids));
        }

        return spec;
    }

    public Specification<MataKuliah> entitySearchKurikulum(
                                                  String programStudi,
                                                  String tahunKurikulum
    ) {
        Specification<MataKuliah> spec = notDeleted();

        if (!Strings.isBlank(programStudi)){
            spec = spec.and(byProgramStudi(programStudi));
        }

        if (!Strings.isBlank(tahunKurikulum)){
            spec = spec.and(byTahunKurikulum(tahunKurikulum));
        }

        return spec;
    }
}
