package com.siakad.service;

import com.siakad.dto.request.HasilStudiReqDto;
import com.siakad.dto.response.HasilStudiDto;
import com.siakad.dto.response.TranskipDto;
import com.siakad.entity.HasilStudi;


import java.util.UUID;

public interface HasilStudiService {
    HasilStudiDto getHasilStudi(UUID periodeAkademikId);
    TranskipDto getTranskip();
}
