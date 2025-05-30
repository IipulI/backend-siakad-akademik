package com.siakad.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class KelasRpsId {
    @Column(name = "siak_kelas_kuliah")
    private UUID siakKelasKuliahId;

    @Column(name = "siak_rps_id")
    private UUID siakRpsId;
}
