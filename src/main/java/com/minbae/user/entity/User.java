package com.minbae.user.entity;

import com.minbae.user.dto.UserAddrChangeDto;
import com.minbae.user.dto.UserResponseStoreListDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_idx")
    private long userIdx;

    @Column(nullable = false,name="user_email")
    private String userEmail;

    @Column(nullable = true,name="user_pwd")
    private String userPwd;

    @Column(nullable = false,name="user_nickname")
    private String userNickname;

    @Column(nullable = true,name="user_basic_addr")
    private String userBasicAddr;

    @Column(nullable = true,name="user_detail_addr")
    private String userDetailAddr;

    @Column(nullable = true,name="user_tel")
    private String userTel;

    @Column(nullable = false,name="user_social")
    private String userSocial;

    @Builder
    public User(long userIdx,String userEmail,String userPwd,String userNickName,String userBasicAddr,String userDetailAddr,String userTel,String userSocial){
        this.userIdx=userIdx;
        this.userEmail=userEmail;
        this.userPwd=userPwd;
        this.userNickname=userNickName;
        this.userBasicAddr=userBasicAddr;
        this.userDetailAddr=userDetailAddr;
        this.userTel=userTel;
        this.userSocial=userSocial;
    }

    public User updateAddr(UserAddrChangeDto userAddrChangeDto){
        this.userBasicAddr=userAddrChangeDto.getUserBasicAddr();
        this.userDetailAddr=userAddrChangeDto.getUserDetailAddr();
        return this;
    }
}
