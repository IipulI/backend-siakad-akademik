    package com.siakad.dto.response;

    import lombok.Data;

    import java.util.UUID;

    @Data
    public class MataKuliahResDto {
        private UUID id;
        private String programStudi;
        private String tahunKurikulum;
        private String semester;
        private String nilaiMin;
        private Integer sksTatapMuka;
        private Integer sksPraktikum;
        private Boolean adaPraktikum;
        private Boolean opsiMataKuliah;
        private String kodeMataKuliah;
        private String namaMataKuliah;
        private String jenisMataKuliah;
        private PrasyaratMataKuliahDto prasyaratMataKuliah1;
        private PrasyaratMataKuliahDto prasyaratMataKuliah2;
        private PrasyaratMataKuliahDto prasyaratMataKuliah3;
    }
