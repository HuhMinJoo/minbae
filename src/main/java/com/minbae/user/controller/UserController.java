package com.minbae.user.controller;

import com.minbae.user.exception.UserCommException;
import com.minbae.user.exception.comm.UserExceptionType;
import com.minbae.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequestMapping("/user")
@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @GetMapping("/main")
    public String user_index(){
        return "index";
    }

    //주문 많은 순, 별점 많은 순
    @GetMapping("/store/{categoryKo}/{categoryEn}/{type}")
    public ModelAndView getStoreByCategory(@PathVariable("categoryKo") String categoryKo,
                                           @PathVariable("categoryEn") String categoryEn,
                                           @PathVariable("type") String type) {
        //정해진 카테고리에 포함되지 않을 경우
        List<String> categorys = new ArrayList<>(Arrays.asList("CHICKEN", "CHINESEFOOD", "DESSERT", "BUNSIK", "PIZZA", "JAPANESEFOOD"));
        if(!categorys.contains(categoryEn)) {
            throw new UserCommException(UserExceptionType.NotExistCategoryException);
        }

        ModelAndView mav = new ModelAndView();
        List<Map<String, Object>> store_list = userService.getStoreByCategoryOrderAndStar(categoryEn, type);

        System.out.println("store_list -------------------> " + store_list);

        mav.addObject("store_list", store_list);
        mav.addObject("categoryKo", categoryKo);
        mav.addObject("categoryEn", categoryEn);
        mav.addObject("type", type);
        mav.setViewName("user/category_click");

        return mav;
    }

    //가게 상세 정보
    @GetMapping("/store/detail/{categoryKo}/{store_idx}/{type}")
    public ModelAndView storeDetail(@PathVariable("store_idx") Long store_idx,
                                    @PathVariable("categoryKo") String categoryKo,
                                    @PathVariable("type") String type){

        //해당 가게 상세 정보
        Map<String, Object> selectStore = userService.findStoreById(store_idx);
        System.out.println("selectStore -> " + selectStore);

        //해당 가게 리뷰 개수(사장, 손님)
        Map<String, Object> selectStoreReview = userService.findReviewByCount(store_idx);

        //해당 가게 대표 메뉴 리스트
        List<Map<String, Object>> kingMenu = userService.findReviewBykingMenu(store_idx);

        //해당 가게 메뉴 리스트
        List<Map<String, Object>> menuList = userService.findByMenuList(store_idx);

        ModelAndView mav = new ModelAndView();
        mav.addObject("selectStore", selectStore);
        mav.addObject("selectStoreReview", selectStoreReview);
        mav.addObject("kingMenu", kingMenu);
        mav.addObject("menuList", menuList);
        mav.addObject("categoryKo", categoryKo);
        mav.addObject("type", type);
        mav.setViewName("user/store_detail");

        return mav;
    }

    //선택한 메뉴 담는 페이지
    @GetMapping("/store/menu/detail/{menu_idx}/{categoryKo}")
    public ModelAndView selectMenu(@PathVariable("menu_idx") Long menu_idx,
                                   @PathVariable("categoryKo") String categoryKo){

        //특정 메뉴idx에 해당하는 가게 정보
        Map<String, Object> storeMenuIdx = userService.findStoreByMenuIdx(menu_idx);

        ModelAndView mav = new ModelAndView();
        mav.addObject("storeMenuIdx", storeMenuIdx);
        mav.addObject("categoryKo", categoryKo);
        mav.setViewName("user/munu_click");

        return mav;
    }

    //리뷰 작성 페이지 이동
    @GetMapping("/review/form/{store_idx}/{trade_history_idx}")
    public ModelAndView reviewForm(@PathVariable("store_idx") Long store_idx,
                             @PathVariable("trade_history_idx") Long trade_history_idx){
        ModelAndView mav = new ModelAndView();
        mav.addObject("store_idx", store_idx);
        mav.addObject("trade_history_idx", trade_history_idx);
        mav.setViewName("user/user_review_form");
        return mav;
    }

    //장바구니 상세 페이지 이도
    @GetMapping("/shopping/list")
    public String shoppingBasket(){
        return "user/shopping_basket";
    }

    @GetMapping("/shopping/visible")
    public String shoppingVisible(){
        return "user/shopping_visible";
    }

    //마이페이지 이동
    @GetMapping("/myPage")
    public String myPage(){
        return "user/user_mypage";
    }

    @GetMapping("/review/management/{user_idx}")
    public ModelAndView reviewManagement(@PathVariable("user_idx") Long user_idx){

        ModelAndView mav = new ModelAndView();
        List<Map<String, Object>> reviewList = userService.reviewList(user_idx);
        Map<String, Object> reviewCnt = userService.reviewCnt(user_idx);

        mav.addObject("reviewList", reviewList);
        mav.addObject("reviewCnt", reviewCnt);
        mav.setViewName("user/review_management");

        return mav;
    }

    //주문 내역 리스트로 이동
    @GetMapping("/order/history/{user_idx}")
    public ModelAndView orderHistory(@PathVariable("user_idx") Long user_idx){

        ModelAndView mav = new ModelAndView();
        mav.setViewName("user/uesr_order_history");

        return mav;
    }

    //로그인
    @GetMapping("/login/form")
    public String userLoginForm(){
        return "user/user_login_form";
    }

    //결제 페이지 이동
    @GetMapping("/order/page/{user_idx}")
    public ModelAndView orderPage(@PathVariable("user_idx") Long user_idx){

        ModelAndView mav = new ModelAndView();
        Map<String, Object> user_info = userService.get_user_info(user_idx);

        mav.addObject("user_info", user_info);
        mav.setViewName("user/user_order_page");
        return mav;
    }

    //주문 현황 페이지
    @GetMapping("/order/detail")
    public String orderDetail(){

        return "user/user_order_detail";
    }

    //img
    @GetMapping(value = "/image/{image_name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> img_search(@PathVariable("image_name") String image_name) throws IOException {

        System.out.println("image_name -> " + image_name);

        InputStream imageStream = new FileInputStream("/home/ec2-user/minbae/C:/이젠/upload/" + image_name);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();
        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
    }

    @GetMapping("/kakao/tel")
    public String kakao_tel_write(){
        return "user/kakao_tel_write";
    }

}
