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
public class KomposisiNilaiMataKuliahId {

    @Column(name = "siak_mata_kuliah_id")
    private UUID siakMataKuliahId;

    @Column(name = "siak_komposisi_nilai_id")
    private UUID siakKomposisiNilaiId;

}


