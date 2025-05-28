package com.siakad.service.impl;

import com.siakad.dto.response.ProgramStudiResDto;
import com.siakad.dto.transform.ProgramStudiTransform;
import com.siakad.entity.ProgramStudi;
import com.siakad.repository.ProgramStudiRepository;
import com.siakad.service.ProgramStudiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProgramStudiServiceImpl implements ProgramStudiService {

    private final ProgramStudiRepository programStudiRepository;
    private final ProgramStudiTransform mapper;

    @Override
    public List<ProgramStudiResDto> getAll() {
        List<ProgramStudi> all = programStudiRepository.findAllByIsDeletedFalse();
        return all.stream().map(mapper::toDto).toList();
    }
}
