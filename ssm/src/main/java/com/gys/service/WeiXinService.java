package com.gys.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WeiXinService {

    private static Logger logger = LoggerFactory.getLogger(WeiXinService.class);

    public String init(String msg_signature, String timestamp, String nonce, String echostr) {


        return null;
    }
}
