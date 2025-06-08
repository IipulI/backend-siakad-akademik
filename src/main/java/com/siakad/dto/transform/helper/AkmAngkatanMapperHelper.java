package com.siakad.dto.transform.helper;

import com.siakad.dto.response.AkmAngkatanDto;
import com.siakad.repository.MahasiswaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AkmAngkatanMapperHelper {

    private final MahasiswaRepository mahasiswaRepository;

    public List<AkmAngkatanDto> getAkmAngkatanList(List<String> listAngkatan){
        List<AkmAngkatanDto> result = new ArrayList<>();

        for (String angkatan : listAngkatan) {
            int aktif = mahasiswaRepository.countByAngkatanAndStatus(angkatan, "AKTIF");
            int cuti = mahasiswaRepository.countByAngkatanAndStatus(angkatan, "CUTI");
            int nonAktif = mahasiswaRepository.countByAngkatanAndStatus(angkatan, "NON_AKTIF");

            AkmAngkatanDto dto = new AkmAngkatanDto();
            dto.setAngkatan(angkatan);
            dto.setAktif(aktif);
            dto.setCuti(cuti);
            dto.setNonAktif(nonAktif);
            dto.setTotal(aktif + cuti + nonAktif);

            result.add(dto);
        }

        return result;
    }
}
