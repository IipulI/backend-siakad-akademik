    package com.siakad.repository;

    import com.siakad.entity.ProgramStudi;
    import jdk.dynalink.linker.LinkerServices;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.List;
    import java.util.Optional;
    import java.util.UUID;

    @Repository
    public interface ProgramStudiRepository extends JpaRepository<ProgramStudi, UUID> {
        Optional<ProgramStudi> findByIdAndIsDeletedFalse(UUID id);

        List<ProgramStudi> findAllByIsDeletedFalse();
    }
