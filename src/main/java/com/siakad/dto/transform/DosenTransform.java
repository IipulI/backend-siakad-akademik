package com.siakad.dto.transform;

import com.siakad.dto.response.DosenDto;
import com.siakad.dto.response.DosenResDto;
import com.siakad.entity.Dosen;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DosenTransform {

    DosenResDto toDto(Dosen entity);

}
