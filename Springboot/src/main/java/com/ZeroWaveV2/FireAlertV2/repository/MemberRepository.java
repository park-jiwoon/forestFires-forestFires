package com.ZeroWaveV2.FireAlertV2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ZeroWaveV2.FireAlertV2.dto.MemberDto;
import com.ZeroWaveV2.FireAlertV2.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByHp(String hp);
    
    Member findByUserName(String userName);
    
    boolean existsByHp(String hp);
    
    Optional<Member> findByHpAndUserName(String hp, String username);
    
    @Query("SELECT new com.ZeroWaveV2.FireAlertV2.dto.MemberDto(m.hp, m.userName, m.createDate) FROM Member m")
    List<MemberDto> findMemberSummary();
}
