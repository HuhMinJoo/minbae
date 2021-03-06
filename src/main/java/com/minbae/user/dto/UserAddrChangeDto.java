package com.minbae.user.dto;

import com.minbae.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
public class UserAddrChangeDto {

    private Long userIdx;

    private String userBasicAddr;

    private String userDetailAddr;

    public User toEntity(){
        return User.builder()
                .userIdx(userIdx)
                .userBasicAddr(userBasicAddr)
                .userDetailAddr(userDetailAddr)
                .build();
    }
}
