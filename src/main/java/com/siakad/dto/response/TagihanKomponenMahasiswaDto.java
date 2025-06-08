package com.siakad.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class TagihanKomponenMahasiswaDto {
    private List<TagihanKomponenDto> tagihanKomponen;
    private BigDecimal totalTagihan;
}
