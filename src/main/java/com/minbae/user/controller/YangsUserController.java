package com.minbae.user.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class YangsUserController {

    @GetMapping("/user/main/addr/main")
    public String mainPageAddrPage(){
        return "addr/main_page_addr_change_view_page";
    }
    @GetMapping("/user/main/addr/edit")
    public String mainPageAddrEditPage(){
        return "addr/main_page_addr_change_view_page_edit";
    }
    @GetMapping("/user/main/addr/map")
    public String mainPageAddrMapPage(String addr,Model model){
        if(addr!=null)model.addAttribute("basicAddr",addr);
        return "addr/main_page_addr_change_view_page_map";
    }
    @GetMapping("/user/main/addr/search")
    public String mainPageSearchPage(){
        return "addr/main_page_addr_change_view_page_search";
    }
    @GetMapping("/user/main/addr/search/result")
    public ModelAndView mainPageSearchResultPage(String basic, String road, ModelAndView model){
        model.addObject("basic",basic);
        model.addObject("road",road);
        model.setViewName("addr/main_page_addr_change_view_page_search_result");
        return model;
    }

    @GetMapping("/join/user")
    public String joinUser(){return "user/join/join_agreement";}

    @GetMapping("/join/user/phone")
    public String joinUserPhoneCheck(){return "user/join/join_phone_check";}
    @GetMapping("/join/user/email")
    public String joinUserEmailCheck(@Param("tel") String tel,Model model){
        model.addAttribute("tel",tel);
        return "user/join/join_email_check";
    }
}
