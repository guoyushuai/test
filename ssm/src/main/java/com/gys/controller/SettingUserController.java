package com.gys.controller;

import com.gys.exception.NoFoundException;
import com.gys.pojo.Role;
import com.gys.pojo.User;
import com.gys.service.UserService;
import com.gys.util.db.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping("/setting/user")
public class SettingUserController {

    @Autowired
    private UserService userService;

    /**
     * get方法，请求该页面时就要显示数据，默认显示不限制名字q_name与角色q_role的第一页p=1数据
     *@RequestParam (相当于request.getparameter())从表单或者URL中获得参数时，
     *该参数非必需或者有默认值(必须是字符串类型)时才加此注解
     * 表单中name属性名为q_name,q_role分别对应java中的参数名queryName,queryRole
     */
    @GetMapping
    public String List(@RequestParam(required = false,defaultValue = "1") Integer p,
                       @RequestParam(required = false,defaultValue = "",name = "q_name") String queryName,
                       @RequestParam(required = false,defaultValue = "",name = "q_role") String queryRole,
                       Model model) throws UnsupportedEncodingException {

        /*List<User> userList = userService.findAllUsers();
        model.addAttribute("userList",userList);*/
        //分页显示所有用户
        /*Page<User> page = userService.findUserByPageNo(p);*/

        //搜索框输入中文,通过url传值导致乱码
        if(StringUtils.isNotEmpty(queryName)) {
            queryName = new String(queryName.getBytes("ISO8859-1"),"UTF-8");
        }

        Page<User> page = userService.findUserByPageNoAndSearchParam(p,queryName,queryRole);

        //供搜索下拉框使用
        List<Role> roleList = userService.findAllRoles();

        model.addAttribute("roleList",roleList);
        model.addAttribute("page",page);
        //保证点下一页时，搜索框依然有值
        model.addAttribute("queryName",queryName);
        model.addAttribute("queryRole",queryRole);
        return "setting/user/list";
    }

    /*@GetMapping("/new")
    public String newUser() {
        return "user/new";
    }*/
    @GetMapping("/new")
    public String newUser(Model model) {
        List<Role> roleList = userService.findAllRoles();
        model.addAttribute("roleList",roleList);
        return "setting/user/new";
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
        return "redirect:/setting/user";
    }

    @GetMapping("/{id:\\d+}/del")
    public String del(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        userService.del(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/setting/user";
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
            return "setting/user/edit";
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
        return "redirect:/setting/user";
    }

}
