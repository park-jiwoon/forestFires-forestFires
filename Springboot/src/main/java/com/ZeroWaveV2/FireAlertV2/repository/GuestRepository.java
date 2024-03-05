package com.ZeroWaveV2.FireAlertV2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ZeroWaveV2.FireAlertV2.model.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest, String> {
    Guest findByHp(String hp);
    boolean existsByHp(String hp);
}

