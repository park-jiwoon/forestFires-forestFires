package com.ZeroWaveV2.FireAlertV2.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ZeroWaveV2.FireAlertV2.dto.FireSituationRoomDto;
import com.ZeroWaveV2.FireAlertV2.model.FireReception;
import com.ZeroWaveV2.FireAlertV2.model.FireSituationRoom;
import com.ZeroWaveV2.FireAlertV2.repository.FireReceptionRepository;
import com.ZeroWaveV2.FireAlertV2.repository.FireSituationRoomRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class FireSituationRoomService {
// 240303 수정
   private final FireSituationRoomRepository fireSituationRoomRepository;
    private final FireReceptionRepository fireReceptionRepository; // 추가
    private final ReverseGeocodingService reverseGeocodingService;
    
    @Autowired
    public FireSituationRoomService(FireSituationRoomRepository fireSituationRoomRepository,
                                    FireReceptionRepository fireReceptionRepository, // 추가
                                    ReverseGeocodingService reverseGeocodingService) {
        this.fireSituationRoomRepository = fireSituationRoomRepository;
        this.fireReceptionRepository = fireReceptionRepository; // 추가
        this.reverseGeocodingService = reverseGeocodingService;
    }


    @Transactional(readOnly = true)
    public List<FireSituationRoomDto> findFireSituationRoomsByFs(String fs) {
        List<FireSituationRoom> rooms = fireSituationRoomRepository.findByFireStationFs(fs);
        return rooms.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    private FireSituationRoomDto convertToDto(FireSituationRoom room) {
        FireSituationRoomDto dto = new FireSituationRoomDto();
        dto.setCommand(room.getCommand());
        dto.setState(room.getState());

        if (room.getFireReception() != null) {
            dto.setImgurl(room.getFireReception().getImgurl());
            dto.setGps(room.getFireReception().getGps());
            dto.setAdate(room.getFireReception().getAdate());
            dto.setProgress(room.getFireReception().getProgress());

            String address = getAddressIfNecessary(room.getFireReception());
            dto.setAddress(address);
        }
        return dto;
    }
    
    @Transactional
    private String getAddressIfNecessary(FireReception fireReception) {
        if (fireReception.getAddress() == null || fireReception.getAddress().isEmpty()) {
            String[] gpsCoordinates = fireReception.getGps().split(",");
            double latitude = Double.parseDouble(gpsCoordinates[0]);
            double longitude = Double.parseDouble(gpsCoordinates[1]);
            String address = reverseGeocodingService.getAddress(latitude, longitude);
            fireReception.setAddress(address);
            fireReception = fireReceptionRepository.findByNum(fireReception.getNum()).orElse(fireReception);
            fireReceptionRepository.saveAndFlush(fireReception);
            return address;
        }
        return fireReception.getAddress();
    }

 // 240303 수정
    
    public Optional<FireReception> findFireReceptionByCommand(int command) {
        Optional<FireSituationRoom> roomOptional = fireSituationRoomRepository.findByCommand(command);
        if (roomOptional.isPresent()) {
            // FireSituationRoom이 존재할 경우, 연결된 FireReception을 반환
            return Optional.of(roomOptional.get().getFireReception());
        } else {
            // FireSituationRoom이 존재하지 않을 경우, 빈 Optional 반환
            return Optional.empty();
        }
    }
    
    //@Transactional(readOnly = true)
    public List<FireSituationRoomDto> findFireSituationRoomsByAdmin() {
    	return fireSituationRoomRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }
}