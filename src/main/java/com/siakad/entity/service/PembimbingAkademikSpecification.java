    package com.siakad.entity.service;

    import com.siakad.entity.PembimbingAkademik;
    import com.siakad.util.QuerySpecification;
    import org.apache.logging.log4j.util.Strings;
    import org.springframework.data.jpa.domain.Specification;

    public class PembimbingAkademikSpecification extends QuerySpecification<PembimbingAkademik> {

        private Specification<PembimbingAkademik> byIsDeleted() {
            return attributeEqual("isDeleted", false);
        }

        private Specification<PembimbingAkademik> byPeriodeAkademik(String param) {
            return attributeContains("siakPeriodeAkademik.namaPeriode", param);
        }

        private Specification<PembimbingAkademik> byProgramStudi(String param) {
            return attributeContains("siakMahasiswa.siakProgramStudi.namaProgramStudi", param);
        }

        private Specification<PembimbingAkademik> bySemester(Integer param) {
            return attributeEqual("siakMahasiswa.semester", param);
        }

        private Specification<PembimbingAkademik> byAngkatan(String param) {
            return attributeEqual("siakMahasiswa.angkatan", param);
        }

        public Specification<PembimbingAkademik> entitySearch(String periodeAkademik, String programStudi, Integer semester, String angkatan) {
            Specification<PembimbingAkademik> spec = byIsDeleted();
            if(!Strings.isBlank(periodeAkademik)) {
                spec = spec.and(byPeriodeAkademik(periodeAkademik));
            }
            if (!Strings.isBlank(programStudi)) {
                spec = spec.and(byProgramStudi(programStudi));
            }
            if (semester != null) {
                spec = spec.and(bySemester(semester));
            }

            if (!Strings.isBlank(angkatan)) {
                spec = spec.and(byAngkatan(angkatan));
            }

            return spec;
        }
    }
