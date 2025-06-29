package com.siakad.dto.response;

import com.siakad.entity.Jenjang;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
public class JenjangResDto {
    private UUID id;
    private String nama;
    private String jenjang;


    // Specific constructor for direct DTO projection from query results
    public JenjangResDto(UUID id, String nama, String jenjang) {
        this.id = id;
        this.nama = nama;
        this.jenjang = jenjang;
    }

    public static JenjangResDto fromEntity(Jenjang entity) {
        if (entity == null) {
            return null;
        }

        return JenjangResDto.builder()
                .id(entity.getId())
                .nama(entity.getNama())
                .jenjang(entity.getJenjang())
                .build();
    }
}