package com.siakad.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TagihanMahasiswaResDto {
    private String tanggal;
    private String kodeTagihan;
    private String npm;
    private String nama;
    private String jenisTagihan;
    private BigDecimal nominal;
    private BigDecimal bayar;
    private Boolean lunas;
}
