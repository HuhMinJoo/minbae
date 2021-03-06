package com.minbae.user.controller;

import com.minbae.user.comm.UserApiResponse;
import com.minbae.user.comm.UserApiStatus;
import com.minbae.user.dto.UserReviewDTO;
import com.minbae.user.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    //사용자 리뷰 작성
    @PostMapping("/review/create")
    public UserApiResponse reviewCreate(MultipartHttpServletRequest req, UserReviewDTO dto) throws Exception {

        List<MultipartFile> upload = req.getFiles("upload");

        int InsertResult = userService.reviewCreate(dto, upload);

        return (InsertResult == 1) ?
                new UserApiResponse(UserApiStatus.SUCCESS, dto) :
                new UserApiResponse(UserApiStatus.FAIL, dto);
    }

    @DeleteMapping("/review/delete/{review_idx}")
    public UserApiResponse reviewDel(@PathVariable("review_idx") String review_idx){

        int deleteResult = userService.reviewDel(review_idx);


        return (deleteResult == 1) ?
        new UserApiResponse(UserApiStatus.SUCCESS, deleteResult) :
        new UserApiResponse(UserApiStatus.FAIL, deleteResult);
    }

    //특정 유저의 주문 내역 리스트
    @GetMapping("/order/history/get/{user_idx}")
    public UserApiResponse reviewList(@PathVariable("user_idx") Long user_idx){

        List<Map<String, Object>> orderHistoryList = userService.orderHistory(user_idx);
//        System.out.println(orderHistoryList);

        return (orderHistoryList != null) ?
                new UserApiResponse(UserApiStatus.SUCCESS, orderHistoryList) :
                new UserApiResponse(UserApiStatus.FAIL, null);
    }

    @GetMapping("/review/state/{trade_history_idx}")
    public UserApiResponse reviewState(@PathVariable("trade_history_idx") Long trade_history_idx){
        Map<String, Object> reviewState = userService.reviewState(trade_history_idx);

        return (reviewState != null) ?
                new UserApiResponse(UserApiStatus.SUCCESS, reviewState) :
                new UserApiResponse(UserApiStatus.FAIL, null);
    }

    @GetMapping("store/detail/review/{store_idx}")
    public UserApiResponse storeDetailReview(@PathVariable("store_idx") Long store_idx){

        List<Map<String, Object>> sd_review_list = userService.storeDetailReview(store_idx);
        return (sd_review_list != null) ?
                new UserApiResponse(UserApiStatus.SUCCESS, sd_review_list) :
                new UserApiResponse(UserApiStatus.FAIL, null);
    }

    //결제
    @PostMapping("/payment")
    public UserApiResponse payment(@RequestBody Map<String, Object> map) throws Exception{

        Map<String, Integer> result = userService.payment(map);

        return (result.get("dbInsertResult") > 0) ?
                new UserApiResponse(UserApiStatus.SUCCESS, result.get("trade_history_idx")) :
                new UserApiResponse(UserApiStatus.FAIL, result);
    }

    @GetMapping("/store/minimum/price/{store_idx}")
    public UserApiResponse minimum_price(@PathVariable("store_idx") Long store_idx){
        String minimum_price = userService.minimum_price(store_idx);

        return new UserApiResponse(minimum_price != null ? UserApiStatus.SUCCESS : UserApiStatus.FAIL, minimum_price);

    }

    //주문 현황 값 받는
    @GetMapping("order/state/{user_idx}")
    public UserApiResponse order_state(@PathVariable Integer user_idx){

        List<Map<String, String>> order_store = userService.get_order_store(user_idx);

        return new UserApiResponse(order_store != null ? UserApiStatus.SUCCESS : UserApiStatus.FAIL, order_store);
    }

    @GetMapping("/store/{store_idx}")
    public UserApiResponse store_location(@PathVariable Integer store_idx){

        Map<String, Integer> store_location = userService.get_store_lication(store_idx);

        return new UserApiResponse(store_location != null ? UserApiStatus.SUCCESS : UserApiStatus.FAIL, store_location);
    }

    //사용자 위도, 경도 받는 api
    @GetMapping("{lat}/{lng}")
    public String user_lat_lng(@PathVariable Double lat, @PathVariable Double lng){

        userService.put_lat_lng(lat, lng);
        return "ok";
    }

    @GetMapping("/trade_history_idx")
    public UserApiResponse tradeHistoryIdx(){
        int trade_history_idx = userService.trade_history_idx();
        System.out.println(trade_history_idx);

        return  (trade_history_idx != 0) ?
                new UserApiResponse(UserApiStatus.SUCCESS, trade_history_idx) :
                new UserApiResponse(UserApiStatus.FAIL, null);
    }

    //카카오 로그인
    @PostMapping("/login")
    public UserApiResponse login(@RequestBody Map<String, Object> param) {

        return new UserApiResponse(UserApiStatus.SUCCESS, userService.kakaoLogin(param));
    }

    //카카오 로그인 시 전화번호 저장
    @PatchMapping("/kakao/tel/update/{user_idx}")
    public UserApiResponse tel_update(@RequestBody Map<String, String> user_tel,
                                      @PathVariable Long user_idx){

        int tel_update_result = userService.tel_update(user_tel, user_idx);

        return new UserApiResponse(tel_update_result != 0 ? UserApiStatus.SUCCESS : UserApiStatus.FAIL, tel_update_result);
    }

}
