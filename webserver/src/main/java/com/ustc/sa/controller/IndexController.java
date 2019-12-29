package com.ustc.sa.controller;

import com.ustc.sa.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    @RequestMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @RequestMapping({"/login"})
    public String toLogin(Model model) {
        model.addAttribute("msg", "请登录!");
        return "login";
    }

    @PostMapping("/toLogin")
    public String login(User user, Model model) {
        //获得当前用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            //执行登录的方法,如果没有异常说明ok
            subject.login(token);
        } catch (UnknownAccountException e) {//用户名不存在
            model.addAttribute("msg", "用户名错误!");
            System.out.println("用户名错");
            return "login";
        } catch (IncorrectCredentialsException e) {//密码错误
            model.addAttribute("msg", "密码错误!");
            System.out.println("密码错");
            return "login";
        }
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("user", user);
        return "redirect:index";
    }

    /**
     * 注销登录
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session, Model model) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        model.addAttribute("msg", "安全退出！");
        return "login";
    }
}
