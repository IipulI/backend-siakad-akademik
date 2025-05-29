package com.siakad.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.DecimalFormat;

@Data
@AllArgsConstructor
public class StatistikTagihanFakultasDto {
    private String namaFakultas;
    @JsonIgnore
    private double persentase;

    public String getPersentaseFormatted() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(persentase) + "%";
    }
}
