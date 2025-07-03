//package com.siakad.dto.transform;
//
//import com.siakad.dto.request.KrsReqDto;
//import com.siakad.dto.request.PesertaKelasReqDto;
//import com.siakad.dto.request.PindahKelasReqDto;
//import com.siakad.dto.response.KrsMenungguResDto;
//import com.siakad.dto.response.KrsResDto;
//import com.siakad.dto.response.MataKuliahResDto;
//import com.siakad.dto.transform.helper.JadwalKuliahMapperHelper;
//import com.siakad.entity.JadwalKuliah;
//import com.siakad.entity.KrsMahasiswa;
//import com.siakad.entity.KrsRincianMahasiswa;
//import com.siakad.entity.MataKuliah;
//import org.mapstruct.AfterMapping;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.MappingTarget;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring")
//@Component
//public abstract class KrsTransform {
//
//    @Autowired
//    private JadwalKuliahMapperHelper jadwalKuliahHelper;
//
//    public abstract KrsMahasiswa toEntity(PesertaKelasReqDto dto);
//    public abstract KrsRincianMahasiswa toEntityRincian(KrsReqDto dto);
//    public abstract KrsRincianMahasiswa toEntityRincianPeserta(PesertaKelasReqDto dto);
//
//    @Mapping(source = "siakKelasKuliah.siakMataKuliah", target = "mataKuliah")
//    @Mapping(source = "hurufMutu", target = "riwayatMatakuliah")
//    @Mapping(source = "siakKelasKuliah.nama", target = "namaKelas")
//    @Mapping(source = "siakKelasKuliah.id", target = "id")
//    @Mapping(target = "hari", ignore = true)
//    @Mapping(target = "jamMulai", ignore = true)
//    @Mapping(target = "jamSelesai", ignore = true)
//    @Mapping(target = "dosenPengajar", ignore = true)
//    public abstract KrsResDto toDto(KrsRincianMahasiswa entity);
//
//    public abstract List<KrsResDto> toDto(List<KrsRincianMahasiswa> entityList);
//
//    @Mapping(source = "siakTahunKurikulum.tahun", target = "tahunKurikulum")
//    @Mapping(source = "siakProgramStudi.namaProgramStudi", target = "programStudi")
//    public abstract MataKuliahResDto mataKuliahDto(MataKuliah entity);
//
//    public abstract List<MataKuliahResDto> mataKuliahDtoList(List<MataKuliah> entityList);
//
//    public abstract List<JadwalKuliah> jadwalKuliahList(List<JadwalKuliah> entityList);
//
//    @AfterMapping
//    protected void afterMapping(@MappingTarget KrsResDto dto, KrsRincianMahasiswa entity) {
//        jadwalKuliahHelper.mapJadwalKuliahToDto(dto, entity);
//    }
//
//    public KrsMenungguResDto toDtoMenunggu(List<KrsRincianMahasiswa> rincianList) {
//        KrsMenungguResDto dto = new KrsMenungguResDto();
//
//        List<KrsResDto> krsList = toDto(rincianList);
//        dto.setKrs(krsList);
//
//        if (!rincianList.isEmpty()) {
//            KrsMahasiswa krs = rincianList.get(0).getSiakKrsMahasiswa();
//            dto.setTotalSks(krs.getJumlahSksDiambil());
//        } else {
//            dto.setTotalSks(0);
//        }
//        return dto;
//    }
//}


package com.siakad.dto.transform;

import com.siakad.dto.request.KrsReqDto;
import com.siakad.dto.request.PesertaKelasReqDto;
import com.siakad.dto.request.PindahKelasReqDto;
import com.siakad.dto.response.KrsMenungguResDto;
import com.siakad.dto.response.KrsResDto;
import com.siakad.dto.response.MataKuliahResDto;
import com.siakad.dto.response.KelasKuliahWithTakenStatusDto;
import com.siakad.dto.response.PrasyaratMataKuliahDto;
import com.siakad.dto.transform.helper.JadwalKuliahMapperHelper;
import com.siakad.entity.*;
import com.siakad.repository.MataKuliahRepository; // NEW: Import MataKuliahRepository
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
@Component
public abstract class KrsTransform {

    @Autowired
    private JadwalKuliahMapperHelper jadwalKuliahHelper;

    // NEW: Inject MataKuliahRepository for prerequisite hydration
    protected MataKuliahRepository mataKuliahRepository;

    @Autowired
    public void setMataKuliahRepository(MataKuliahRepository mataKuliahRepository) {
        this.mataKuliahRepository = mataKuliahRepository;
    }


    // --- ORIGINAL CORE METHODS (Copied from your "working" file, unchanged) ---
    public abstract KrsMahasiswa toEntity(PesertaKelasReqDto dto);
    public abstract KrsRincianMahasiswa toEntityRincian(KrsReqDto dto);
    // Assuming this one takes PesertaKelasReqDto as in your latest working code
    public abstract KrsRincianMahasiswa toEntityRincianPeserta(PesertaKelasReqDto dto);

    @Mapping(source = "siakKelasKuliah.siakMataKuliah", target = "mataKuliah")
    @Mapping(source = "hurufMutu", target = "riwayatMatakuliah")
    @Mapping(source = "siakKelasKuliah.nama", target = "namaKelas")
    @Mapping(source = "siakKelasKuliah.id", target = "id")
    @Mapping(target = "hari", ignore = true)
    @Mapping(target = "jamMulai", ignore = true)
    @Mapping(target = "jamSelesai", ignore = true)
    @Mapping(target = "dosenPengajar", ignore = true)
    public abstract KrsResDto toDto(KrsRincianMahasiswa entity);

    public abstract List<KrsResDto> toDto(List<KrsRincianMahasiswa> entityList);

    @Mapping(source = "siakTahunKurikulum.tahun", target = "tahunKurikulum")
    @Mapping(source = "siakProgramStudi.namaProgramStudi", target = "programStudi")
    public abstract MataKuliahResDto mataKuliahDto(MataKuliah entity);

    public abstract List<MataKuliahResDto> mataKuliahDtoList(List<MataKuliah> entityList);

    // Removed the problematic 'jadwalKuliahList' abstract method based on previous discussions.
    // public abstract List<JadwalKuliah> jadwalKuliahList(List<JadwalKuliah> entityList);

    @AfterMapping
    protected void afterMapping(@MappingTarget KrsResDto dto, KrsRincianMahasiswa entity) {
        if (jadwalKuliahHelper != null) { // Defensive check
            jadwalKuliahHelper.mapJadwalKuliahToDto(dto, entity);
        }
    }

    // This method is concrete, so it should NOT be 'abstract'.
    @Mapping(source = "siakMataKuliah", target = "mataKuliah")
    @Mapping(source = "nama", target = "namaKelas")
    @Mapping(target = "hari", ignore = true)
    @Mapping(target = "jamMulai", ignore = true)
    @Mapping(target = "jamSelesai", ignore = true)
    @Mapping(target = "dosenPengajar", ignore = true)
    public abstract KrsResDto toDtoKelas(KelasKuliah entity);


    @AfterMapping
    protected void afterMapping(@MappingTarget KrsResDto dto, KelasKuliah entity) {
        jadwalKuliahHelper.mapJadwalKuliahKelasToDto(dto, entity);
    }

    public KrsMenungguResDto toDtoMenunggu(List<KrsRincianMahasiswa> rincianList) {
        KrsMenungguResDto dto = new KrsMenungguResDto();

        List<KrsResDto> krsList = toDto(rincianList); // Uses the base toDto mapping
        dto.setKrs(krsList);

        if (!rincianList.isEmpty()) {
            KrsMahasiswa krs = rincianList.get(0).getSiakKrsMahasiswa();
            dto.setTotalSks(krs.getJumlahSksDiambil());
        } else {
            dto.setTotalSks(0);
        }
        return dto;
    }


    // --- NEW METHODS FOR KELAS KULIAH CATALOG FEATURE ---

    // NEW MAPPING METHOD: From our internal query projection DTO to KrsResDto
    @Mapping(source = "namaKelas", target = "namaKelas")
    @Mapping(source = "id", target = "id") // Maps KelasKuliah.id to KrsResDto.id
    @Mapping(source = "hari", target = "hari")
    @Mapping(source = "jamMulai", target = "jamMulai")
    @Mapping(source = "jamSelesai", target = "jamSelesai")
    @Mapping(source = "dosenPengajar", target = "dosenPengajar")
    @Mapping(target = "riwayatMatakuliah", ignore = true) // Handled in new @AfterMapping
    // Nested MataKuliahResDto mapping: direct fields from the projection DTO
    @Mapping(source = "mataKuliahId", target = "mataKuliah.id")
    @Mapping(source = "kodeMataKuliah", target = "mataKuliah.kodeMataKuliah")
    @Mapping(source = "namaMataKuliah", target = "mataKuliah.namaMataKuliah")
    @Mapping(source = "semesterMataKuliah", target = "mataKuliah.semester") // Maps Integer to String
    @Mapping(source = "jenisMataKuliah", target = "mataKuliah.jenisMataKuliah")
    @Mapping(source = "sksTatapMuka", target = "mataKuliah.sksTatapMuka")
    @Mapping(source = "sksPraktikum", target = "mataKuliah.sksPraktikum")
    // Re-added mappings for these fields, as they were missing in your "working" code's projection mapping
    @Mapping(source = "nilaiMin", target = "mataKuliah.nilaiMin")
    @Mapping(source = "adaPraktikum", target = "mataKuliah.adaPraktikum")
    @Mapping(source = "opsiMataKuliah", target = "mataKuliah.opsiMataKuliah")
    @Mapping(source = "programStudiNama", target = "mataKuliah.programStudi") // Maps String from projection to String in DTO
    @Mapping(source = "tahunKurikulumTahun", target = "mataKuliah.tahunKurikulum") // Maps String from projection to String in DTO
    // Prerequisite MataKuliah fields will be handled in a dedicated @AfterMapping (ignored during direct mapping)
    @Mapping(target = "mataKuliah.prasyaratMataKuliah1", ignore = true)
    @Mapping(target = "mataKuliah.prasyaratMataKuliah2", ignore = true)
    @Mapping(target = "mataKuliah.prasyaratMataKuliah3", ignore = true)
    public abstract KrsResDto toKrsResDtoFromKelasKuliahProjection(KelasKuliahWithTakenStatusDto projectionDto);

    // Helper for mapping a list of projection DTOs
    public List<KrsResDto> toKrsResDtoListFromKelasKuliahProjection(List<KelasKuliahWithTakenStatusDto> projectionDtoList) {
        return projectionDtoList.stream()
                .map(this::toKrsResDtoFromKelasKuliahProjection)
                .collect(Collectors.toList());
    }


    // NEW @AfterMapping for riwayatMatakuliah field (for the new KrsResDto mapping from projection)
    @AfterMapping
    protected void setRiwayatMatakuliahForCatalog(@MappingTarget KrsResDto targetDto, KelasKuliahWithTakenStatusDto sourceDto) {
        if (sourceDto.isTakenByCurrentUser()) {
            if (sourceDto.getLastTakenNilai() != null) {
                // Correctly convert BigDecimal to String for KrsResDto.riwayatMatakuliah
                targetDto.setRiwayatMatakuliah(sourceDto.getLastTakenNilai());
            } else {
                targetDto.setRiwayatMatakuliah("");
            }
        } else {
            targetDto.setRiwayatMatakuliah(""); // Set to empty string if not taken
        }
    }

    // NEW @AfterMapping to hydrate nested PrasyaratMataKuliahDto objects (N+1 queries likely)
    @AfterMapping
    protected void hydratePrerequisiteMataKuliah(@MappingTarget KrsResDto targetDto, KelasKuliahWithTakenStatusDto sourceDto) {
        // Defensive check for repository injection
        if (mataKuliahRepository == null) {
            System.err.println("MataKuliahRepository not injected for prerequisite hydration. Prerequisites will be null.");
            return;
        }

        // Declare optionalMataKuliah here, before the if/else logic
        Optional<MataKuliah> optionalMataKuliahEntity = mataKuliahRepository.findById(sourceDto.getMataKuliahId());

        if (optionalMataKuliahEntity.isPresent()) {
            MataKuliah mataKuliahEntity = optionalMataKuliahEntity.get();

            // Map each prerequisite MataKuliah entity to its PrasyaratMataKuliahDto
            if (mataKuliahEntity.getPrasyaratMataKuliah1() != null) {
                targetDto.getMataKuliah().setPrasyaratMataKuliah1(
                        prasyaratMataKuliahDto(mataKuliahEntity.getPrasyaratMataKuliah1())
                );
            }
            if (mataKuliahEntity.getPrasyaratMataKuliah2() != null) {
                targetDto.getMataKuliah().setPrasyaratMataKuliah2(
                        prasyaratMataKuliahDto(mataKuliahEntity.getPrasyaratMataKuliah2())
                );
            }
            if (mataKuliahEntity.getPrasyaratMataKuliah3() != null) {
                targetDto.getMataKuliah().setPrasyaratMataKuliah3(
                        prasyaratMataKuliahDto(mataKuliahEntity.getPrasyaratMataKuliah3())
                );
            }
        }
        // If optionalMataKuliahEntity is not present, the prerequisite DTOs will remain null,
        // which is the desired behavior for a non-existent main MataKuliah.
    }

    // NEW: Abstract mapping method for PrasyaratMataKuliahDto (from MataKuliah entity)
    // This is used by the hydratePrerequisiteMataKuliah method.
    @Mapping(source = "id", target = "id")
    @Mapping(source = "kodeMataKuliah", target = "kodeMataKuliah")
    @Mapping(source = "namaMataKuliah", target = "namaMataKuliah")
    public abstract PrasyaratMataKuliahDto prasyaratMataKuliahDto(MataKuliah entity);
}