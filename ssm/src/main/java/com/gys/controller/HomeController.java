package com.gys.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    //默认的登录页在xml中配置为/
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/",method = RequestMethod.POST)
    public String login(String username, String password, RedirectAttributes redirectAttributes) {
        //Shiro方式登录,触发ShiroDbRealm中的登录认证
        Subject subject = SecurityUtils.getSubject();
        try {
            //传入认证的token(即根据什么认证，ShiroDbRealm中的登录认证)；根据login方法是否抛异常来判断登录成功还是失败
            subject.login(new UsernamePasswordToken(username, password));
            return "redirect:/home";
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message","账号或密码错误");
            return "redirect:/";
        }
    }


    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/403")
    public String error403() {
        return "403";
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message","您已安全退出");
        return "redirect:/";
    }

}
