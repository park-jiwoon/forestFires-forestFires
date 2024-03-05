package com.ZeroWaveV2.FireAlertV2.service;

import com.ZeroWaveV2.FireAlertV2.model.Guest;
import com.ZeroWaveV2.FireAlertV2.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuestService {

    private final GuestRepository guestRepository;

    @Autowired
    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    // 전화번호로 비회원 조회
    public Guest findGuestByHp(String hp) {
        return guestRepository.findByHp(hp);
    }

    // 비회원 생성 또는 조회
    public Guest createOrFindGuest(String hp) {
        // 전화번호로 기존 비회원 조회
        Guest guest = findGuestByHp(hp);
        if (guest == null) {
            // 존재하지 않으면 새로운 비회원 생성
            guest = new Guest();
            guest.setHp(hp);
            // 비회원의 기본 userLevel을 설정
            guest.setUserLevel("0"); // "0"이 비회원을 의미한다고 가정
            guestRepository.save(guest);
        }
        return guest;
    }
 // 비회원 정보 업데이트
    public Guest updateGuestInfo(String hp, String newUserLevel) {
        Guest guest = findGuestByHp(hp);
        if (guest != null) {
            guest.setUserLevel(newUserLevel);
            guestRepository.save(guest);
        }
        return guest;
    }


    // 기타 필요한 비회원 관련 비즈니스 로직을 여기에 추가합니다.
}
