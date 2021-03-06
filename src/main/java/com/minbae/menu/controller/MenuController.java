package com.minbae.menu.controller;

import com.minbae.menu.entity.Menu;
import com.minbae.menu.service.MenuService;
import com.minbae.store.entity.Store;
import com.minbae.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;
import java.io.*;

@RequiredArgsConstructor
@Controller
public class MenuController {

    public final MenuService menuService;
    public final StoreService storeService;

    // 특정 가게의 메뉴관리 페이지 이동 및 메뉴 조회
    @GetMapping("/owner/menu/{storeIdx}")
    public String menuListPage(@PathVariable String storeIdx, Model model){
        Long storeIdxL = Long.valueOf(storeIdx);
        List<Menu> menuList = menuService.getStoreMenuList(storeIdxL);
        model.addAttribute("menuList", menuList);

        Store storeEntity = storeService.storeInfo(storeIdxL);
        model.addAttribute("storeEntity", storeEntity);
        return "menu/menu_list";
    }

    @PostMapping("/owner/menu/{storeIdx}")
    public String menuListPagePost(@PathVariable String storeIdx, Model model){
        Long storeIdxL = Long.valueOf(storeIdx);
        List<Menu> menuList = menuService.getStoreMenuList(storeIdxL);
        model.addAttribute("menuList", menuList);

        Store storeEntity = storeService.storeInfo(storeIdxL);
        model.addAttribute("storeEntity", storeEntity);
        return "menu/menu_list";
    }

    @GetMapping("/owner/menuSunbun/{storeIdx}")
    public String menuSunbunListPage(@PathVariable String storeIdx, Model model){
        String storeName = storeService.storeInfo(Long.valueOf(storeIdx)).getStoreName();
        model.addAttribute("storeName", storeName);
        return "menu/menu_list_sortable";
    }

    // 특정 가게의 신규메뉴 등록 페이지 이동
    @GetMapping("/owner/menu/create/{storeIdx}")
    public String menuCreate(@PathVariable Long storeIdx, Model model){
        String storeName = storeService.storeInfo(storeIdx).getStoreName();
        model.addAttribute("storeIdx",storeIdx);
        model.addAttribute("storeName",storeName);

        return "menu/menu_create_form";
    }

    // 이미지 출력
    @GetMapping(value = "/image/{imagename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> imageSearch(@PathVariable("imagename") String imagename) throws IOException {
        //InputStream imageStream = new FileInputStream("C:\\workspace\\upload\\" + imagename);
        InputStream imageStream = new FileInputStream("/home/ec2-user/minbae/C:/이젠/upload//" + imagename);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();
        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
    }


}
