package com.ZeroWaveV2.FireAlertV2.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ZeroWaveV2.FireAlertV2.dto.FireSituationRoomDto;
import com.ZeroWaveV2.FireAlertV2.model.FireReception;
import com.ZeroWaveV2.FireAlertV2.service.FireSituationRoomService;

@RestController
@RequestMapping("/api/fireSituationRoom")
public class FireSituationRoomController {
    @Autowired
    private FireSituationRoomService fireSituationRoomService;


    @GetMapping
    public ResponseEntity<List<FireSituationRoomDto>> getFireSituationRooms(@AuthenticationPrincipal UserDetails currentUser) {
       String fs = currentUser.getUsername();
       
       List<FireSituationRoomDto> fireSituationRoom = fireSituationRoomService.findFireSituationRoomsByFs(fs);
        
       if (fireSituationRoom.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(fireSituationRoom);
    }
    
    @GetMapping("/{command}")
    public ResponseEntity<?> getFireReceptionByCommand(@PathVariable int command) {
        Optional<FireReception> receptionOptional = fireSituationRoomService.findFireReceptionByCommand(command);
        if (receptionOptional.isPresent()) {
            return ResponseEntity.ok(receptionOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/admin")
    public List<FireSituationRoomDto> getAllResult() {
        return fireSituationRoomService.findFireSituationRoomsByAdmin();
    }
}