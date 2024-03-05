package com.ZeroWaveV2.FireAlertV2.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ZeroWaveV2.FireAlertV2.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByHp(String hp);
}
