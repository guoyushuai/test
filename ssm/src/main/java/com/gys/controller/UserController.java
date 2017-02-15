package com.gys.controller;

import com.gys.exception.NoFoundException;
import com.gys.pojo.Role;
import com.gys.pojo.User;
import com.gys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String List(Model model) {
        List<User> userList = userService.findAllUsers();
        model.addAttribute("userList",userList);
        return "user/list";
    }

    /*@GetMapping("/new")
    public String newUser() {
        return "user/new";
    }*/
    @GetMapping("/new")
    public String newUser(Model model) {
        List<Role> roleList = userService.findAllRoles();
        model.addAttribute("roleList",roleList);
        return "user/new";
    }

    /*@PostMapping("/new")
    public String newUser(User user, RedirectAttributes redirectAttributes) {
        userService.save(user);
        *//*保存成功在页面上给用户提示（非弹窗）*//*
        redirectAttributes.addFlashAttribute("message","保存成功");
        *//*注意重定向*//*
        return "redirect:/user";
    }*/
    @PostMapping("/new")
    public String newUser(User user,Integer[] roleids, RedirectAttributes redirectAttributes) {
        userService.saveNewUser(user,roleids);
        redirectAttributes.addFlashAttribute("message","保存成功");
        return "redirect:/user";
    }

    @GetMapping("/{id:\\d+}/del")
    public String del(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        userService.del(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/user";
    }

    @GetMapping("/{id:\\d+}/edit")
    public String edit(@PathVariable Integer id,Model model) {
        User user = userService.findUserById(id);

        //查找用户的同时做连接查询
        //查找用户所对应的角色集合

        if(user == null) {
            //自定义异常
            throw new NoFoundException();
        } else {
            //编辑用户时需要将所有的角色选项列出
            List<Role> roleList = userService.findAllRoles();
            model.addAttribute("roleList",roleList);
            model.addAttribute("user",user);
            return "user/edit";
        }
    }

    /*//url中的id并没有接收使用
    @PostMapping("/{id:\\d+}/edit")
    public String edit(User user,RedirectAttributes redirectAttributes) {
        userService.edit(user);
        redirectAttributes.addFlashAttribute("message","修改成功");
        return "redirect:/user";
    }*/
    @PostMapping("/{id:\\d+}/edit")
    public String edit(User user,Integer[] roleids,RedirectAttributes redirectAttributes) {
        userService.edit(user,roleids);
        redirectAttributes.addFlashAttribute("message","修改成功");
        return "redirect:/user";
    }

}
