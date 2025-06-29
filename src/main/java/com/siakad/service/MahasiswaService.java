package com.siakad.service;

import com.siakad.dto.request.MahasiswaReqDto;
import com.siakad.dto.response.MahasiswaChartDto;
import com.siakad.dto.response.MahasiswaResDto;
import com.siakad.entity.User;
import com.siakad.enums.RoleType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface MahasiswaService {
    MahasiswaResDto create(MahasiswaReqDto request, MultipartFile fotoProfil, MultipartFile ijazahSekolah, HttpServletRequest servletRequest) throws IOException;
    Page<MahasiswaResDto> getPaginated(
            String keyword,
            String programStudi,
            String jenisPendaftaran,
            String kelasPerkuliahan,
            String angkatan,
            String jalurPendaftaran,
            String statusMahasiswa,
            String gelombang,
            String jenisKelamin,
            String sistemKuliah,
            String kurikulum,
            String periodeMasuk,
            String periodeKeluar,
            int page, int size
    );
    byte[] getFotoProfil(UUID id);
    byte[] getIjazahSekolah(UUID id);
    MahasiswaResDto getOne(UUID id);
    MahasiswaResDto update(UUID id, MultipartFile fotoProfil, MultipartFile ijazahSekolah, MahasiswaReqDto request, HttpServletRequest servletRequest) throws IOException;
    void delete(UUID id, HttpServletRequest servletRequest);
    User createUserWithRole(String username, String email, String password, RoleType roleType);

    MahasiswaChartDto getDashboardAkademik(UUID mahasiswaId);
}   
