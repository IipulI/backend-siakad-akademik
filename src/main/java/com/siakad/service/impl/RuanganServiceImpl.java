package com.siakad.service.impl;

import com.siakad.dto.response.RuanganResDto;
import com.siakad.dto.transform.ProgramStudiTransform;
import com.siakad.entity.Ruangan;
import com.siakad.repository.RuanganRepository;
import com.siakad.service.RuanganService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuanganServiceImpl implements RuanganService {

    private final RuanganRepository ruanganRepository;
    private final ProgramStudiTransform mapper;

    @Override
    public List<RuanganResDto> getAll() {
        List<Ruangan> all = ruanganRepository.findAllByIsDeletedFalse();
        return all.stream().map(mapper::toDto).toList();
    }
}
