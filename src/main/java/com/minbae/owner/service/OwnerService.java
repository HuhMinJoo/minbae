package com.minbae.owner.service;

import com.minbae.owner.dao.OwnerMapper;
import com.minbae.owner.dto.OwnerKakaoDto;
import com.minbae.owner.dto.OwnerRequestDTO;
import com.minbae.owner.dto.OwnerResponseDTO;
import com.minbae.owner.entity.Owner;
import com.minbae.owner.repository.OwnerRepository;
import com.minbae.sso.comm.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    public OwnerResponseDTO loginOwner(OwnerRequestDTO ownerRequestDTO){
        String ownerEmail = ownerRequestDTO.getOwnerEmail();
        String ownerPwd = ownerRequestDTO.getOwnerPwd();

        // 아이디 비밀번호 조회
        Owner target = ownerRepository.findByOwnerEmailAndOwnerPwd(ownerEmail, ownerPwd);

        // entity -> dto
        OwnerResponseDTO ownerResponseDTO = OwnerResponseDTO.toOwnerResponseDTO(target);
        return ownerResponseDTO;
    }

    // 0514 kakao하면서 추가 - kakaoid (pwd), kakaoEmail 로 owner 테이블에서 사장찾기
    public OwnerResponseDTO findOwnerDto(String kakaoOwnerEmail, String ownerPwd){
        Owner target = ownerRepository.findByOwnerEmailAndOwnerPwd(kakaoOwnerEmail, ownerPwd);
        if(target==null){
            return null;
        }
        OwnerResponseDTO ownerResponseDTO = OwnerResponseDTO.toOwnerResponseDTO(target);
        return ownerResponseDTO;
    }

    // 0514 kakao하면서 추가 -
    public void joinOwnerByKakao(OwnerKakaoDto ownerKakaoDto){
        // dto -> entity
        Owner target = Owner.toEntity(ownerKakaoDto);
        // entity db save
        ownerRepository.save(target);
    }

    //카카오 로그인
    @Transactional
    public Map<String, Object> kakaoLogin(Map<String, Object> param) {

        String kakaoEmail = (String) param.get("email");
        if(kakaoEmail == null || kakaoEmail.equals("")){
            param.remove("email");
            param.put("email", param.get("pwd")+"@kakao.noemail");
        }

        // 카카오 회원 체크
        Map<String, Object> kakaoUser = ownerMapper.findByKakaoEmailAndKakoPwd(param.get("email"), param.get("pwd"));

        // 없으면 insert
        if(kakaoUser == null) {
            ownerMapper.insertKakaoOwner(param.get("email"), param.get("nickName"), param.get("pwd"));
            Map<String, Object> kakaoNewUser = ownerMapper.findByKakaoEmailAndKakoPwd(param.get("email"), param.get("pwd"));
            return kakaoNewUser;
        }


        return kakaoUser;
    }

}
