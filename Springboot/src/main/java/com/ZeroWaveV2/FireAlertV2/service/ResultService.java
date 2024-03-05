package com.ZeroWaveV2.FireAlertV2.service;

import com.ZeroWaveV2.FireAlertV2.model.FireSituationRoom;
import com.ZeroWaveV2.FireAlertV2.model.Result;
import com.ZeroWaveV2.FireAlertV2.dto.ResultDto;
import com.ZeroWaveV2.FireAlertV2.repository.FireSituationRoomRepository;
import com.ZeroWaveV2.FireAlertV2.repository.ResultRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;  
    
    @Autowired
    private FireSituationRoomRepository fireSituationRoomRepository;

    @Transactional
    public Result createResult(ResultDto resultDto) {
        // resultDto에서 command를 가져옴
        Integer command = resultDto.getCommand();
        
        // command를 사용하여 FireSituationRoom을 조회
        FireSituationRoom fireSituationRoom = fireSituationRoomRepository.findById(command).orElse(null);
        if (fireSituationRoom == null) {
            // FireSituationRoom을 찾을 수 없는 경우, 예외 처리 또는 적절한 로직 구현
            throw new RuntimeException("FireSituationRoom not found with command: " + command);
        }
        
        // 새로운 Result 객체 생성 및 속성 설정
        Result result = new Result();
        result.setDtime(resultDto.getDtime());
        result.setCdate(resultDto.getCdate());
        result.setWtime(resultDto.getWtime());
        result.setFf(resultDto.getFf());
        result.setFtruck(resultDto.getFtruck());
        result.setHc(resultDto.getHc());
        result.setFw(resultDto.getFw());
        result.setLosses(resultDto.getLosses());
        result.setLmoney(resultDto.getLmoney());
        result.setDarea(resultDto.getDarea());
        result.setFireSituationRoom(fireSituationRoom); // FireSituationRoom 참조 설정
        
        // Result 객체 저장
        result = resultRepository.save(result);
       
        return result;
    }
    
    public ResultDto getResultByCommand(int command) {
    	Result result = resultRepository.findByFireSituationRoom_Command(command);
        if (result != null) {
        	return convertEntityToDto(result);
        }        
        return null;
    }
    
    @Transactional
    public ResultDto updateResult(int command, ResultDto resultDto) {
        // 데이터베이스에서 command에 해당하는 Result 찾기
        Result result = resultRepository.findByFireSituationRoom_Command(command);
        result.setDtime(resultDto.getDtime());
        result.setCdate(resultDto.getCdate());
        result.setWtime(resultDto.getWtime());
        result.setFf(resultDto.getFf());
        result.setFtruck(resultDto.getFtruck());
        result.setHc(resultDto.getHc());
        result.setFw(resultDto.getFw());
        result.setLosses(resultDto.getLosses());
        result.setLmoney(resultDto.getLmoney());
        result.setDarea(resultDto.getDarea());
        fireSituationRoomRepository.findByCommand(command).ifPresent(fireSituationRoom -> {
            if (fireSituationRoom.getFireReception() != null) {
                fireSituationRoom.getFireReception().setProgress("진화 완료");
                // JPA's automatic dirty checking will update the FireReception entity
            }
        });
        
        result = resultRepository.save(result);
       
        return convertEntityToDto(result);
    }
    
    private ResultDto convertEntityToDto(Result result) {
    	ResultDto resultDTO = new ResultDto();
    	resultDTO.setDispatch(result.getDispatch());
    	resultDTO.setDtime(result.getDtime());
        resultDTO.setCdate(result.getCdate());
        resultDTO.setWtime(result.getWtime());
        resultDTO.setFf(result.getFf());
        resultDTO.setFtruck(result.getFtruck());
        resultDTO.setHc(result.getHc());
        resultDTO.setFw(result.getFw());
        resultDTO.setLosses(result.getLosses());
        resultDTO.setLmoney(result.getLmoney());
        resultDTO.setDarea(result.getDarea());
        return resultDTO;
    }
}