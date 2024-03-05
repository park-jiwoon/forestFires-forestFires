package com.ZeroWaveV2.FireAlertV2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ZeroWaveV2.FireAlertV2.dto.ResultDto;
import com.ZeroWaveV2.FireAlertV2.model.Result;
import com.ZeroWaveV2.FireAlertV2.service.ResultService;

@RestController
@RequestMapping("/api/result")
public class ResultController {
    
    @Autowired
    private ResultService resultService;
    
    @GetMapping("/{command}")
    public ResponseEntity<ResultDto> getResultByCommand(@PathVariable int command) {
       ResultDto resultDto = resultService.getResultByCommand(command);
        if (resultDto != null) {
           return ResponseEntity.ok(resultDto);
        } else {
           return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Result> createResult(@RequestBody ResultDto resultDto) {       
        Result result = resultService.createResult(resultDto);        
        return ResponseEntity.ok(result);
        
    }
    
    @PutMapping("/{command}")
    public ResponseEntity<ResultDto> updateResult(@PathVariable int command, @RequestBody ResultDto resultDto) {
        try {
            ResultDto updatedResult = resultService.updateResult(command, resultDto);
            return ResponseEntity.ok(updatedResult);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}