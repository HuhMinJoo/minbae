package com.minbae.menu.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.minbae.menu.dto.MenuSaveRequestDto;
import com.minbae.menu.entity.Menu;
import com.minbae.menu.service.MenuService;
import com.minbae.store.entity.Store;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
public class MenuRestController {

    private final MenuService menuService;

    // 특정 사장님 신규 메뉴 등록
    @PostMapping(value = "/create/menu", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Menu> createMenu(@RequestPart(value = "key") MenuSaveRequestDto requestDto,
                                           @RequestPart(value = "file",required = false) MultipartFile file) throws IOException {

        Menu savedResult = menuService.createMenu(requestDto, file);
        return (savedResult != null) ?
                ResponseEntity.status(HttpStatus.CREATED).body(savedResult)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //@ResponseBody // restcontroller = responsebody+controller 합쳐진것이라 적지 않아도 됨.
    @PatchMapping(value = "/update/menuImage")
    public ResponseEntity<Integer> deleteMenuImage(@RequestBody HashMap<String,String> param){

        String menuImg = param.get("menuImg");
        Long menuIdx = Long.valueOf(param.get("menuIdx")); // map 에서 가져온 value를 강제형변환 할 수 없는것 같다. valueof 메소드를 사용해야한다고 한다.

        int deleteImgResultNum = menuService.deleteMenuImageOnly(menuImg, menuIdx);
        System.out.println("이미지삭제결과>>>"+deleteImgResultNum);

        return (deleteImgResultNum != 0) ?
                ResponseEntity.status(HttpStatus.OK).body(deleteImgResultNum)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PatchMapping(value = "/update/menuInfo", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Menu> updateMenu(@RequestPart(value = "key") MenuSaveRequestDto requestDto,
                                           @RequestPart(value = "file", required = false) MultipartFile file) throws Exception{

        System.out.println("왜값을 못가져오니"+file.getOriginalFilename());
        Menu updatedResult = menuService.updateMenu(requestDto, file);

        return (updatedResult != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updatedResult)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // delete menu
    @DeleteMapping(value = "/owner/delete/menu/{menuIdx}")
    public ResponseEntity<Menu> deleteMenu(@PathVariable Long menuIdx){
        Menu result = menuService.delete(menuIdx);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // api로 메뉴리스트 응답
    @GetMapping("/owner/menuList/sunbun/{storeIdx}")
    public ResponseEntity menuSunbunPage(@PathVariable String storeIdx, Model model){
        List<Menu> menuList = menuService.getStoreMenuListOrderSunbun(Long.valueOf(storeIdx));
        return (menuList != null) ? ResponseEntity.status(HttpStatus.OK).body(menuList)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PatchMapping("/owner/menuList/sunbunPatch/{storeIdx}")
    public ResponseEntity<Integer> menuSunbunChange(@PathVariable String storeIdx, @RequestBody List<String> sunbunList){
        //System.out.println(sunbunList);
        Integer resultNum = menuService.changeMenuSunbun(storeIdx, sunbunList);
        return (resultNum == 1) ? ResponseEntity.status(HttpStatus.OK).body(resultNum)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultNum);
    }

}
