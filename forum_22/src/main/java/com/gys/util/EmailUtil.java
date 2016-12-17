package com.gys.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtil {

    private static Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    public static void sendHtmlEmail(String subject,String htmlMsg,String addTo) {

        //创建对象
        HtmlEmail email = new HtmlEmail();
        //服务器地址
        email.setHostName(Config.get("email.hostname"));
        //服务器端口
        email.setSmtpPort(Integer.valueOf(Config.get("email.smtpport")));
        //登录账号密码
        email.setAuthentication(Config.get("email.username"),Config.get("email.password"));
        //字符编码
        email.setCharset("UTF-8");
        //安全协议transport layer security
        email.setStartTLSEnabled(true);

        System.out.println();

        try {
            //发送人
            email.setFrom(Config.get("email.from"));
            //主题
            email.setSubject(subject);
            //内容
            email.setHtmlMsg(htmlMsg);
            //收件人
            email.addTo(addTo);
            //发送
            email.send();
        } catch (EmailException e) {
            logger.error("向{}发送邮件失败",addTo);
            throw new RuntimeException("向" + addTo + "发送邮件失败",e);
        }

    }
}
