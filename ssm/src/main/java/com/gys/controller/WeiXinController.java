package com.gys.controller;

import com.gys.service.WeiXinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/wx")
public class WeiXinController {

    private Logger logger = LoggerFactory.getLogger(WeiXinController.class);

    @Autowired
    private WeiXinService weiXinService;

    /**
     * 微信初始化方法
     * 验证应用开启回调模式时URL的有效性（get请求，接收传递过来的四个参数）
     * @return
     */
    @GetMapping("/init")
    @ResponseBody
    public String init(String msg_signature,String timestamp,String nonce,String echostr) {
        logger.info("msg_signature:{}--timestamp:{}--nonce:{}--echostr:{}",msg_signature,timestamp,nonce,echostr);
        return weiXinService.init(msg_signature,timestamp,nonce,echostr);
    }


}
