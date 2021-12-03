package com.example.SpringMVCApplication;

import com.example.SpringMVCApplication.domain.User;
import com.example.SpringMVCApplication.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class GreetingController {
    @Autowired
    private UserRepository userRepository;
    @Value("${upload.path}")
    private String uploadPath;
    @GetMapping("/")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name,
                           HttpServletRequest request, HttpServletResponse response,
                           Map<String, Object> model) {
        return "redirect:/greeting";
    }
    @PostMapping("/greeting")
    public String exit(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(name="name", required=false, defaultValue="World") String name,
                       Map<String, Object> model){
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies){
            Cookie cookie = new Cookie(c.getName(), "");
            c.setPath("/");
            c.setMaxAge(0);
            response.addCookie(cookie);
        }
        model.put("name", name);
        return "redirect:/";
    }
    @GetMapping("/greeting")
    public String greetingPage(HttpServletRequest request, HttpServletResponse response,
                               //@RequestParam(name="name", required=false, defaultValue="World") String name,
                               Map<String, Object> model) {
        Cookie[] cookies = request.getCookies();
        String cookie = "cookieName";
        Cookie cookieName = new Cookie("cookieName", "World!");
        if (cookies!=null) {
            for (Cookie c : cookies) {
                if (cookie.equals(c.getName())) {
                    cookieName = c;
                    break;
                }
            }
        }
        response.addCookie(cookieName);
        if (cookieName.getValue()=="")
            model.put("name", "World");
        else
            model.put("name", cookieName.getValue());
        return "greeting";
    }
    @GetMapping("/main")
    public String main(Map<String, Object> model){
        Iterable<User> users = userRepository.findAll();
        model.put("users", users);
        return "main";
    }
    /*
    Добавь freemarker
    не работает раздача картинок
     */
    @PostMapping("/main")
    public String adUser(@RequestParam("file") MultipartFile file, @RequestParam String name, @RequestParam String password,
                         @RequestParam String gender, @RequestParam Integer age, Map<String, Object> model,
                         HttpServletResponse response) throws IOException {
        User user = new User(name, password, gender, age);
        response.addCookie(new Cookie("cookieName", name));
        response.addCookie(new Cookie("cookiePassword", password));
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()){
            uploadDir.mkdir();
        }
        if (file != null){
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName =  uuidFile + file.getOriginalFilename();
            user.setFilename(resultFileName);
            System.out.println(uploadPath + "\\" + resultFileName);
            file.transferTo(new File(uploadPath + "\\" + resultFileName));
            //System.out.println(resultFileName);
           // file.transferTo(new File(resultFileName));
        }
        if (file==null)
            System.out.print("aaaaaaaaaaaaaaaaaaaaa");
        userRepository.save(user);
        return "redirect:/users";
    }

}