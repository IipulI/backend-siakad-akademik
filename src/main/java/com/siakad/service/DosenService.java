package com.siakad.service;

import com.siakad.dto.response.DosenResDto;

import java.util.List;

public interface DosenService {

    List<DosenResDto> getAll(String keyword);
}
