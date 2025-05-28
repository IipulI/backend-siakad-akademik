package com.siakad.service.impl;

import com.siakad.dto.response.DosenResDto;
import com.siakad.dto.transform.DosenTransform;
import com.siakad.entity.Dosen;
import com.siakad.entity.service.DosenSpecification;
import com.siakad.repository.DosenRepository;
import com.siakad.service.DosenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DosenServiceImpl implements DosenService {

    private final DosenRepository dosenRepository;
    private final DosenTransform mapper;

    @Override
    public List<DosenResDto> getAll(String keyword) {
        DosenSpecification specBuilder = new DosenSpecification();
        Specification<Dosen> spec = specBuilder.entitySearch(keyword);
        List<Dosen> all = dosenRepository.findAll(spec);
        return all.stream().map(mapper::toDto).toList();
    }
}
