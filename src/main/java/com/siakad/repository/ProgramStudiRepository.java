    package com.siakad.repository;

    import com.siakad.entity.ProgramStudi;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


    import java.util.List;
    import java.util.Optional;
    import java.util.UUID;

    public interface ProgramStudiRepository extends JpaRepository<ProgramStudi, UUID>, JpaSpecificationExecutor<ProgramStudi> {
        Optional<ProgramStudi> findByIdAndIsDeletedFalse(UUID id);
        List<ProgramStudi> findAllByIsDeletedFalse();
    }

