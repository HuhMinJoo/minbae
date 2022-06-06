package com.minbae.comm.stomp.store;

import com.minbae.comm.tradehistory.service.TradeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class StoreStompController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final TradeHistoryService tradeHistoryService;

    // 주문자 -> 가게
    @MessageMapping("/store/{storeIdx}")
    public void greeting(UserToStoreMessage message, @DestinationVariable("storeIdx") String storeIdx) throws Exception {
        simpMessagingTemplate.convertAndSend("/topic/store/"+storeIdx, message);
    }

    // 가게 -> 주문자 (수락/거절)
    @MessageMapping("/user/{userIdx}")
    public void ordering(StoreToUserMessage message,  @DestinationVariable("userIdx") String userIdx) throws Exception{
        //Thread.sleep(500);
        simpMessagingTemplate.convertAndSend("/topic/user/"+userIdx, message);
        tradeHistoryService.changeOrderState(message);
    }

    // 가게 -> 기사
//    @MessageMapping("/deliver")
//    public void requestDelivering(StoreToDeliverMessage message) throws Exception{
//        //Thread.sleep(500);
//        simpMessagingTemplate.convertAndSend("/app/chat/assign", message);
//    }

}