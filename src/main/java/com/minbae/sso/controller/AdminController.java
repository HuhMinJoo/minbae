package com.minbae.sso.controller;


import com.minbae.sso.comm.ApiResponse;
import com.minbae.sso.comm.ApiStatus;
import com.minbae.sso.jwt.JwtTokenProvider;
import com.minbae.sso.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@Api(tags = "A. Login api", description = "로그인 관련 api")
public class AdminController {

    private final AdminService adminService;

    @ApiOperation(value = "로그인", notes = "로그인 api")
    @GetMapping("/login/{role}")
    public ApiResponse login(@PathVariable String role,String email,String pwd) {
        String token = adminService.login(role,email,pwd);
        return new ApiResponse(token!=null?ApiStatus.SUCCESS:ApiStatus.FAIL, token,adminService.getMemberInfo(role,email,pwd));
    }
}
