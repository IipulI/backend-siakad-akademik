package com.siakad.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class MengulangResDto {
    private String namaMataKuliah;
    private String kodeMataKuliah;
    private List<PeriodeResDto> periode;
}
