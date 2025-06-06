package com.siakad.dto.transform.helper;

import com.siakad.dto.response.JenjangResDto;
import com.siakad.dto.response.ProgramStudiResDto;
import com.siakad.entity.ProgramStudi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgramStudiMapperHelper {

    public ProgramStudiResDto toDto(ProgramStudi entity) {
        JenjangResDto jenjangDto = new JenjangResDto();
        jenjangDto.setId(entity.getSiakJenjang().getId());
        jenjangDto.setNama(entity.getSiakJenjang().getNama());
        jenjangDto.setJenjang(entity.getSiakJenjang().getJenjang());

        ProgramStudiResDto dto = new ProgramStudiResDto();
        dto.setId(entity.getId());
        dto.setNamaProgramStudi(entity.getNamaProgramStudi());
        dto.setJenjang(jenjangDto);

        return dto;
    }
}