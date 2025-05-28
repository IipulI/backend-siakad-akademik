package com.siakad.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class KurikulumProdiResDto {
    private String semester;
    List<MataKuliahResDto> mataKuliah;
    private Integer totalSks;
    public Integer getTotalSks() {
        if (mataKuliah == null || semester == null) {
            return 0;
        }
        return mataKuliah.stream()
                .filter(mk -> semester.equalsIgnoreCase(mk.getSemester()))
                .mapToInt(mk ->
                        (mk.getSksTatapMuka() != null ? mk.getSksTatapMuka() : 0) +
                                (mk.getSksPraktikum() != null ? mk.getSksPraktikum() : 0)
                )
                .sum();
    }
}
