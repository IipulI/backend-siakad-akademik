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
@AllArgsConstructor
public class JenjangResDto {
    private UUID id;
    private String nama;
    private String jenjang;

    /**
     * Static factory method untuk mengubah Entity menjadi DTO.
     * @param entity Objek Jenjang dari database.
     * @return Objek JenjangResDto.
     */
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