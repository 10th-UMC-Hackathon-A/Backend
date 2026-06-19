package com.example.umc.domain.penalty.repository;

import com.example.umc.domain.penalty.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

    List<Penalty> findAllByDeletedAtIsNull();

    Optional<Penalty> findByPenaltyIdAndDeletedAtIsNull(Long penaltyId);

    @Modifying(clearAutomatically = true)
    @Query("""
            update Penalty p
            set p.penaltyName = :penaltyName
            where p.penaltyId = :penaltyId
              and p.deletedAt is null
            """)
    int updatePenaltyName(Long penaltyId, String penaltyName);
}
