package com.siakad.dto.transform.helper;

import com.siakad.dto.response.EligiblePesertaKelasDto;
import com.siakad.dto.response.JenjangResDto;
import com.siakad.dto.response.ProgramStudiResDto;
import com.siakad.entity.Mahasiswa;
import com.siakad.entity.ProgramStudi;
import org.springframework.stereotype.Component;

@Component
public class EligibleMahasiswaMapper {

    public EligiblePesertaKelasDto toDto(Mahasiswa mahasiswa, Integer batasSks, Integer sksDiambil) {
        ProgramStudi ps = mahasiswa.getSiakProgramStudi();
        JenjangResDto jenjangDto = JenjangResDto.fromEntity(ps.getSiakJenjang()); // Menggunakan factory method

        ProgramStudiResDto psDto = new ProgramStudiResDto();
        psDto.setId(ps.getId());
        psDto.setNamaProgramStudi(ps.getNamaProgramStudi());
        psDto.setJenjang(jenjangDto);

        return new EligiblePesertaKelasDto(
                mahasiswa.getId(),
                mahasiswa.getNpm(),
                mahasiswa.getNama(),
                mahasiswa.getSemester(),
                psDto,
                batasSks,
                sksDiambil
        );
    }
}