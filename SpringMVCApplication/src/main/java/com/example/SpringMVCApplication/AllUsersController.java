package com.example.SpringMVCApplication;

import com.example.SpringMVCApplication.domain.User;
import com.example.SpringMVCApplication.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class AllUsersController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/users")
    public String showUsers(Map<String, Object> model, HttpServletRequest request,
                            @CookieValue(value = "cookieName", required = false) String cookieName,
                            @RequestParam(name="name", required=false, defaultValue="World") String name){
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        if(cookies !=null) {
            for(Cookie c: cookies) {
                if(cookieName.equals(c.getName())) {
                    cookie = c;
                    break;
                }
            }
        }
        if (cookieName!=""){
            System.out.println(cookieName+" cookie name");
            Iterable<User> users = userRepository.findAll();
            model.put("users", users);
            return "allUsers";
        }
        else {
            /*model.put("name", "World");
            return "greeting";*/
            return "redirect:/";
        }
    }
}
