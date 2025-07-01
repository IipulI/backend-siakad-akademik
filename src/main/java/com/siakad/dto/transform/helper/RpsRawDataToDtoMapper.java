package com.siakad.dto.transform.helper;

import com.siakad.dto.response.RpsDetailResDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Component
public class RpsRawDataToDtoMapper {

    public RpsDetailResDto mapRawDataToRpsDetailResDto(Object[] rawData) {
        Object[] actualDataRow;

        // --- Step 1: Initial Validation and Extraction of the Actual Row Data ---
        if (rawData == null) {
            log.error("mapSingleRawDataRowToRpsDetailResDto received null rawData array.");
            throw new IllegalArgumentException("Raw data cannot be null for RpsDetailResDto transformation.");
        }

        log.info("Mapper received rawData array with length: {}", rawData.length);
        if (rawData.length > 0) {
            log.info("Type of rawData[0]: {}", rawData[0] != null ? rawData[0].getClass().getName() : "null");
        }


        if (rawData.length == 1 && rawData[0] instanceof Object[]) {
            actualDataRow = (Object[]) rawData[0];
            log.warn("Detected and extracted nested array. Actual data row length: {}", actualDataRow.length);
        } else if (rawData.length > 0 && rawData[0] instanceof jakarta.persistence.Tuple) {
            log.error("mapSingleRawDataRowToRpsDetailResDto received a single-element array containing a Tuple. This method is not designed for direct Tuple mapping.");
            throw new IllegalArgumentException("Expected Object[] from native query, but received Tuple inside single-element array. Adjust mapper or query return type.");
        }
        else {
            actualDataRow = rawData;
            log.warn("Mapper did not detect a nested array. Proceeding with rawData as actualDataRow. Length: {}", actualDataRow.length);
        }

        // --- Step 2: Validate the length of the extracted actualDataRow ---
        // UPDATED EXPECTED COLUMN COUNT TO 17 (14 original + 3 for Jenjang fields)
        if (actualDataRow.length != 17) {
            log.error("Invalid actual data row length after extraction. Expected 17 columns, but got {}. Content: {}",
                    actualDataRow.length, Arrays.toString(actualDataRow));
            throw new IllegalArgumentException("Invalid data row length for RpsDetailResDto transformation. Expected 17 columns, but got " + actualDataRow.length + ".");
        }

        // --- Step 3: Perform Type Casting and DTO Construction ---
        try {
            // Mapping order must match the SELECT clause in RpsRepository's native query:
            // 0: rps.id,
            // 1: tk.id, 2: tk.tahun,
            // 3: pa.id, 4: pa.nama_periode, 5: pa.kode_periode,
            // 6: ps.id, 7: ps.nama_program_studi,
            // 8: j.id, 9: j.nama, 10: j.jenjang,
            // 11: rps.tanggal_penyusun, 12: rps.deskripsi_mata_kuliah,
            // 13: rps.tujuan_mata_kuliah, 14: rps.materi_pembelajaran,
            // 15: rps.pustaka_utama, 16: rps.pustaka_pendukung

            UUID rpsId = (UUID) actualDataRow[0];
            UUID tkId = (UUID) actualDataRow[1];
            String tkTahun = (String) actualDataRow[2];
            UUID paId = (UUID) actualDataRow[3];
            String paNamaPeriode = (String) actualDataRow[4];
            String paKodePeriode = (String) actualDataRow[5];
            UUID psId = (UUID) actualDataRow[6];
            String psNamaProgramStudi = (String) actualDataRow[7];

            // Jenjang fields (new indices 8, 9, 10)
            UUID jenjangId = (UUID) actualDataRow[8];
            String jenjangNama = (String) actualDataRow[9];
            String jenjangJenjang = (String) actualDataRow[10];

            // LocalDate (index 11) - FIX FOR java.sql.Date to java.time.LocalDate
            LocalDate tanggalPenyusun = null;
            if (actualDataRow[11] != null) { // Index is now 11
                if (actualDataRow[11] instanceof Date) { // Check if it's java.sql.Date
                    tanggalPenyusun = ((Date) actualDataRow[11]).toLocalDate();
                } else if (actualDataRow[11] instanceof LocalDate) { // Less likely, but handles if already LocalDate
                    tanggalPenyusun = (LocalDate) actualDataRow[11];
                } else {
                    log.error("Unexpected type for tanggalPenyusun at index 11: {}. Expected java.sql.Date or java.time.LocalDate.", actualDataRow[11].getClass().getName());
                    throw new ClassCastException("Cannot cast " + actualDataRow[11].getClass().getName() + " to java.time.LocalDate for tanggalPenyusun.");
                }
            }
            String deskripsiMataKuliah = (String) actualDataRow[12];
            String tujuanMataKuliah = (String) actualDataRow[13];
            String materiPembelajaran = (String) actualDataRow[14];
            String pustakaUtama = (String) actualDataRow[15];
            String pustakaPendukung = (String) actualDataRow[16];

            // Construct and return the DTO using its constructor
            return new RpsDetailResDto(
                    rpsId,
                    tkId, tkTahun,
                    paId, paNamaPeriode, paKodePeriode,
                    psId, psNamaProgramStudi,
                    jenjangId, jenjangNama, jenjangJenjang, // Pass Jenjang fields to RpsDetailResDto constructor
                    tanggalPenyusun, deskripsiMataKuliah,
                    tujuanMataKuliah, materiPembelajaran,
                    pustakaUtama, pustakaPendukung
            );

        } catch (ClassCastException e) {
            log.error("ClassCastException during DTO transformation. This usually indicates a type mismatch during casting from Object[].", e);
            log.error("Problematic raw data causing ClassCastException: {}", Arrays.toString(actualDataRow));
            throw new IllegalArgumentException("Type mismatch during RpsDetailResDto transformation: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("An unexpected error occurred during RpsDetailResDto transformation.", e);
            throw new RuntimeException("Error during DTO transformation: " + e.getMessage(), e);
        }
    }
}