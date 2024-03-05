package com.ZeroWaveV2.FireAlertV2.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ZeroWaveV2.FireAlertV2.dto.MountainDto;
import com.ZeroWaveV2.FireAlertV2.model.Mountain_recommend;
import com.ZeroWaveV2.FireAlertV2.repository.MountainRepository;

@Service
public class MountainService {
	
	@Autowired
	private final MountainRepository mountainRepository;

    public MountainService(MountainRepository mountainRepository) {
        this.mountainRepository = mountainRepository;
    }

    public List<MountainDto> findAllMountains() {
        return mountainRepository.findAll().stream().map(entity -> {
            MountainDto dto = new MountainDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }
    
    public List<MountainDto> findByFilters(String address, String season, String mttime, Integer minHeight, Integer maxHeight) {
        System.out.println("Filtering with address: " + address + ", season: " + season + ", mttime: " + mttime + ", minHeight: " + minHeight + ", maxHeight: " + maxHeight); // 요청 로그 출력
        List<Mountain_recommend> filteredMountains = mountainRepository.findAll().stream()
                .filter(mountain -> address == null || mountain.getAddress().trim().equalsIgnoreCase(address.trim()))
                .filter(mountain -> mttime == null || mountain.getMttime().equalsIgnoreCase(mttime))
                .filter(mountain -> season == null || mountain.getSeason().equalsIgnoreCase(season))
                .filter(mountain -> minHeight == null || mountain.getHeight() >= minHeight)
                .filter(mountain -> maxHeight == null || mountain.getHeight() <= maxHeight)
                .collect(Collectors.toList());

        return filteredMountains.stream().map(mountain -> {
            MountainDto dto = new MountainDto();
            BeanUtils.copyProperties(mountain, dto);
            return dto;
        }).collect(Collectors.toList());
    }
    
    // 주소 데이터를 찾는 메소드
    public List<String> findDistinctAddresses() {
        return mountainRepository.findDistinctAddress();
    }
    
    // 계절 데이터를 찾는 메소드
    public List<String> findDistinctSeasons() {
        return mountainRepository.findDistinctSeason();
    }
    
    // 등산 시간 데이터를 찾는 메소드
    public List<String> findDistinctMtTimes() {
        return mountainRepository.findDistinctMtTime();
    }
    
    // 메인 페이지 등산로 추천
    public Mountain_recommend getRecommendedMountain() {
        String season = getCurrentSeason();
        List<Mountain_recommend> mountains = mountainRepository.findRandomBySeason(season);
        if (!mountains.isEmpty()) {
            return mountains.get(new Random().nextInt(mountains.size()));
        }
        return null;
    }

    private String getCurrentSeason() {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        if (month >= 3 && month <= 5) return "봄";
        else if (month >= 6 && month <= 8) return "여름";
        else if (month >= 9 && month <= 11) return "가을";
        else return "겨울";
    }

}
