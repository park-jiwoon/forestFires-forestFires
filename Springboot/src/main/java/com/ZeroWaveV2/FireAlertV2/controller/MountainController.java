package com.ZeroWaveV2.FireAlertV2.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ZeroWaveV2.FireAlertV2.dto.MountainDto;
import com.ZeroWaveV2.FireAlertV2.model.Mountain_recommend;
import com.ZeroWaveV2.FireAlertV2.service.MountainService;

@RestController
@RequestMapping("/api/mountain")
public class MountainController {

    private final MountainService mountainService;

    public MountainController(MountainService mountainService) {
        this.mountainService = mountainService;
    }

    @GetMapping
    public List<MountainDto> getAllMountains() {
        return mountainService.findAllMountains();
    }
    
    @GetMapping("/filter")
    public List<MountainDto> getMountainsByFilters(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String season,
            @RequestParam(required = false) String mttime,
            @RequestParam(required = false) Integer minHeight,
            @RequestParam(required = false) Integer maxHeight) {
    	System.out.println("Filtering with address: " + address + ", season: " + season + ", mttime: " + mttime + ", minHeight: " + minHeight + ", maxHeight: " + maxHeight);
        return mountainService.findByFilters(address, season, mttime, minHeight, maxHeight);
    }
    
    // 주소 데이터를 가져오는 새로운 API 엔드포인트
    @GetMapping("/addresses")
    public List<String> getDistinctAddresses() {
        return mountainService.findDistinctAddresses();
    }
    
    // 계절 데이터를 가져오는 새로운 API 엔드포인트
    @GetMapping("/seasons")
    public List<String> getDistinctSeasons() {
        return mountainService.findDistinctSeasons();
    }
    
    // 등산 시간 데이터를 가져오는 새로운 API 엔드포인트
    @GetMapping("/mttimes")
    public List<String> getDistinctMtTimes() {
        return mountainService.findDistinctMtTimes();
    }
    
    // 메인 페이지 등산로 추천
    @GetMapping("/main")
    public Mountain_recommend getRecommendation() {
        return mountainService.getRecommendedMountain();
    }
}
