package com.gys.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtil {

    private static Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    public static void sendHtmlEmail(String subject,String htmlMsg,String addTo) {

        HtmlEmail email = new HtmlEmail();
        email.setHostName(Config.get("email.hostname"));
        email.setSmtpPort(Integer.valueOf(Config.get("email.smtpport")));
        email.setAuthentication(Config.get("email.username"),Config.get("email.password"));
        email.setCharset("UTF-8");
        email.setStartTLSEnabled(true);

        try {
            email.setFrom(Config.get("email.from"));
            email.setSubject(subject);
            email.setHtmlMsg(htmlMsg);
            email.addTo(addTo);
            email.send();
        } catch (EmailException e) {
            logger.error("向{}发送邮件失败",addTo);
            throw new RuntimeException("向" + addTo + "发送邮件失败",e);
        }

    }
}
